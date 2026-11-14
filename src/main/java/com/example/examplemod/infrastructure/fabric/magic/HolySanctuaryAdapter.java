package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.WindFieldRuntimeAdapter;

import com.example.examplemod.magic.MagicControl;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.holy.sacred_nova.SacredNovaRules;
import com.example.examplemod.magic.spell.holy.god_purge.GodPurgeRules;

import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** The opening strike happens once; the saved field only affects natural spawns. */
public final class HolySanctuaryAdapter {
	private static final class Runtime {
		private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,Long>> INDEX =
			net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("holy_sanctuary_adapter_runtime"),
				builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,Long>()));
	}
	private static java.util.Map<MagicAreaEntity,Long> states(net.minecraft.world.level.Level level) {
		return level.getAttachedOrCreate(Runtime.INDEX);
	}

	public static boolean cast(ServerPlayer owner, MagicAim aim) {
		return cast(owner, aim, SpellDefinition.SACRED_NOVA, owner.position());
	}

	public static boolean purge(ServerPlayer owner, MagicAim aim) {
		var point = WindFieldRuntimeAdapter.target(owner, SpellDefinition.GOD_PURGE, aim);
		return point != null && cast(owner, aim, SpellDefinition.GOD_PURGE, point);
	}

	public static boolean handles(SpellDefinition spell) { return spell == SpellDefinition.SACRED_NOVA || spell == SpellDefinition.GOD_PURGE; }

	private static boolean cast(ServerPlayer owner, MagicAim aim, SpellDefinition spell, Vec3 center) {
		var level = (ServerLevel) owner.level();
		double radius = spell == SpellDefinition.GOD_PURGE ? GodPurgeRules.RADIUS : SacredNovaRules.RADIUS;
		var area = new MagicAreaEntity(MagicSystem.MAGIC_AREA, level);
		area.configure(owner, spell, aim);
		area.setPos(center);
		if (!AreaRuntimeAdapter.replaceExpansion(owner, area)) return false;
		states(area.level()).put(area, level.getGameTime() + spell.lifetimeTicks());area.setAttached(MagicAttachments.SPIKES_STARTED,level.getGameTime());
		var source = new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(HolyInstantAdapter.DAMAGE), area, owner);
		for (var target : level.getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(radius), e -> HolyInstantAdapter.eligible(owner, e))) {
			var delta = target.getBoundingBox().getCenter().subtract(center);
			com.example.examplemod.magic.mechanic.targeting.HolyOpeningHit.apply(new com.example.examplemod.magic.mechanic.targeting.HolyOpeningHit.Target(){
				public boolean inside(){return delta.lengthSqr()<=radius*radius;}
				public boolean visible(){return SpellVisibilityAdapter.visible(level,center.add(0,.3,0),target,area);}
				public boolean hurt(){return target.hurtServer(level,source,spell.damage());}
				public void afterDamage(){if(spell==SpellDefinition.SACRED_NOVA)target.knockback(.6,-delta.x,-delta.z);}
			});
		}
		tick(area);
		return true;
	}

	public static void tick(MagicAreaEntity area) {
		if (!(area.level() instanceof ServerLevel level)) return;
		long left = states(area.level()).getOrDefault(area, 0L) - level.getGameTime();
		if (left <= 0 || !(area.getOwner() instanceof ServerPlayer owner) || !owner.isAlive()
				|| owner.level() != level || !AreaRuntimeAdapter.adoptExpansion(owner, area)) {
			states(area.level()).remove(area);
			area.discard();
			return;
		}
		if (!SacredNovaRules.ringDue(left)) return;
		var p = area.position();
		var spell = area.spell();
		double radius = spell == SpellDefinition.GOD_PURGE ? GodPurgeRules.ringRadius(spell.lifetimeTicks(),left) : SacredNovaRules.ringRadius(spell.lifetimeTicks(),left);
		for (int i = 0; i < 32; i++) {
			double a = i * Math.PI / 16 + left * .022;
			ParticlePacketAdapter.send(level,ParticleTypes.END_ROD, p.x + radius * Math.cos(a), p.y + .35, p.z + radius * Math.sin(a), 3, .3, .22, .3, .03);
		}
	}

	public static boolean suppresses(ServerLevel level, EntityType<?> type, BlockPos pos) {
		if (!type.builtInRegistryHolder().is(HolyInstantAdapter.TARGETS)
				|| type.builtInRegistryHolder().is(MagicControl.BIND_IMMUNE)) return false;
		for (var entry : states(level).entrySet()) {
			var field = entry.getKey();
			if(com.example.examplemod.magic.mechanic.field.NaturalSpawnSuppression.suppresses(new com.example.examplemod.magic.mechanic.field.NaturalSpawnSuppression.Field(){
				public boolean removed(){return field.isRemoved();}
				public boolean sameLevel(){return field.level()==level;}
				public boolean unexpired(){return entry.getValue()>level.getGameTime();}
				public boolean inside(){double d=field.position().distanceToSqr(Vec3.atCenterOf(pos));return field.spell()==SpellDefinition.GOD_PURGE?GodPurgeRules.inside(d):SacredNovaRules.inside(d);}
			}))return true;
		}
		return false;
	}

	public static void save(MagicAreaEntity area, ValueOutput out) { out.putLong("NovaExpiry", states(area.level()).getOrDefault(area, 0L)); }
	public static void load(MagicAreaEntity area, ValueInput in) { states(area.level()).put(area, in.getLongOr("NovaExpiry", 0)); }
	private HolySanctuaryAdapter() {}
}
