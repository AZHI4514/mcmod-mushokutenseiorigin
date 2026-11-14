package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.HolySanctuaryAdapter;
import com.example.examplemod.infrastructure.fabric.magic.DivineJudgmentAdapter;
import com.example.examplemod.infrastructure.fabric.magic.WindFieldRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.targeting.HolyGeometry;

import java.util.function.Predicate;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.example.examplemod.ExampleMod;

/** Instant holy spells share server-side target selection and obstruction checks. */
public final class HolyInstantAdapter {
	public static final TagKey<EntityType<?>> TARGETS = TagKey.create(Registries.ENTITY_TYPE, ExampleMod.id("holy_targets"));
	public static final ResourceKey<net.minecraft.world.damagesource.DamageType> DAMAGE =
			ResourceKey.create(Registries.DAMAGE_TYPE, ExampleMod.id("magic_holy"));
	private record Cast(Vec3 start, Vec3 end, Vec3 axis, double length, LivingEntity single) {}

	private static Cast plan(ServerPlayer player, SpellDefinition spell, MagicAim aim) {
		if (!aim.valid()) return null;
		var level = (ServerLevel) player.level();
		var start = player.getEyePosition();
		var axis = aim.direction();
		var limit = start.add(axis.scale(spell.range()));
		if (!SpellRaycastAdapter.loaded(level, new AABB(start, limit).inflate(1))) return null;
		var end = level.clip(new ClipContext(start, limit, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getLocation();
		double nearest = start.distanceToSqr(end);
		LivingEntity single = null;
		if (com.example.examplemod.bootstrap.HolyInstantComposition.policy(spell).singleTarget()) {
			for (var candidate : level.getEntitiesOfClass(LivingEntity.class, new AABB(start, end).inflate(1), target -> eligible(player, target))) {
				var hit = candidate.getBoundingBox().clip(start, end);
				if (hit.isPresent() && com.example.examplemod.magic.mechanic.targeting.HolyNearest.nearer(start.distanceToSqr(hit.get()),nearest)) {
					nearest = start.distanceToSqr(hit.get());
					single = candidate;
				}
			}
			if (single == null) return null;
			end = start.add(axis.scale(Math.sqrt(nearest)));
		}
		return new Cast(start, end, axis, start.distanceTo(end), single);
	}

	public static boolean eligible(ServerPlayer player, LivingEntity target) {
		return com.example.examplemod.magic.mechanic.targeting.HolyEligibility.eligible(new com.example.examplemod.magic.mechanic.targeting.HolyEligibility.Target(){
			public boolean player(){return target instanceof Player;}public boolean alive(){return target.isAlive();}public boolean owner(){return target==player;}public boolean tagged(){return target.getType().builtInRegistryHolder().is(TARGETS);}
		});
	}

	public static boolean canCast(ServerPlayer player, SpellDefinition spell, MagicAim aim) {
		return spell == SpellDefinition.SACRED_NOVA ? aim.valid() : (spell == SpellDefinition.DIVINE_JUDGE || spell == SpellDefinition.GOD_PURGE) ? aim.valid() && WindFieldRuntimeAdapter.target(player, spell, aim) != null : plan(player, spell, aim) != null;
	}

	public static boolean inColumn(Vec3 offset, Vec3 axis, double length) {
		return HolyGeometry.column(offset.x,offset.y,offset.z,axis.x,axis.y,axis.z,length);
	}

	public static boolean cast(ServerPlayer player, SpellDefinition spell, MagicAim aim) {
		if (spell == SpellDefinition.GOD_PURGE) return HolySanctuaryAdapter.purge(player, aim);
		if (spell == SpellDefinition.SACRED_NOVA) return HolySanctuaryAdapter.cast(player, aim);
		if (spell == SpellDefinition.DIVINE_JUDGE) return DivineJudgmentAdapter.cast(player, aim);
		var cast = plan(player, spell, aim);
		if (cast == null) return false;
		var level = (ServerLevel) player.level();
		var damage = new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DAMAGE), player);
		if (cast.single() != null) {
			cast.single().hurtServer(level, damage, spell.damage());
		} else {
			var policy=(com.example.examplemod.magic.mechanic.targeting.HolyVolumePolicy)com.example.examplemod.bootstrap.HolyInstantComposition.policy(spell);
			boolean burst=policy.endpointBurst();
			var origin = burst ? cast.end().subtract(cast.axis().scale(.02)) : cast.start();
			var bounds = burst ? new AABB(origin, origin).inflate(4) : new AABB(origin, cast.end()).inflate(2);
			Predicate<Vec3> shape = offset -> policy.shape(offset.x,offset.y,offset.z,cast.axis().x,cast.axis().y,cast.axis().z,cast.length());
			for (var target : level.getEntitiesOfClass(LivingEntity.class, bounds, t -> eligible(player, t))) {
				com.example.examplemod.magic.mechanic.targeting.HolyAreaHit.apply(new com.example.examplemod.magic.mechanic.targeting.HolyAreaHit.Target(){
					public boolean inside(){return shape.test(target.getBoundingBox().getCenter().subtract(origin));}
					public boolean visible(){return SpellVisibilityAdapter.visible(level,origin,target,player);}
					public void hurt(){target.hurtServer(level,damage,spell.damage());}
				});
			}
		}
		visual(level, spell, cast);
		return true;
	}

	private static void visual(ServerLevel level, SpellDefinition spell, Cast cast) {
		var p = cast.end();
		SpellVisuals.burst(level,spell,p,cast.axis());
		if (com.example.examplemod.bootstrap.HolyInstantComposition.policy(spell).visibleBeam()) {
			int samples = Math.max(1, (int) Math.ceil(cast.length() * 2));
			double spread = com.example.examplemod.bootstrap.HolyInstantComposition.policy(spell).beamSpread();
			for (int i = 0; i < samples; i++) {
				var point = cast.start().lerp(p, (i + .5) / samples);
				ParticlePacketAdapter.send(level,ParticleTypes.END_ROD, point.x, point.y, point.z, 2, spread, spread, spread, .025);
			}
		}
		double radius = com.example.examplemod.bootstrap.HolyInstantComposition.policy(spell).burstSpread();
		ParticlePacketAdapter.send(level,ParticleTypes.END_ROD, p.x, p.y, p.z, 30 + 10 * spell.rank(), radius, radius, radius, .14);
		ParticlePacketAdapter.send(level,ParticleTypes.ELECTRIC_SPARK, p.x, p.y, p.z, 14, radius, radius, radius, .09);
	}

	private HolyInstantAdapter() {}
}
