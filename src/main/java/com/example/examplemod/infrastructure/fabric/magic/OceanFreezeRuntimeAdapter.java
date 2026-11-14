package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.water.ocean_freeze.*;

import java.util.HashSet;
import java.util.WeakHashMap;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** A bounded water-only freezing front; terrain restoration belongs to the saved lease queue. */
public final class OceanFreezeRuntimeAdapter {
	private static final class Runtime {
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,OceanFreezeState>> TYPE =
		net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("ocean_freeze_runtime_adapter_runtime"),
			builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,OceanFreezeState>()));
	}

	/** The level owns this transient index; original explicit ValueInput/ValueOutput remains authoritative. */
	private static java.util.Map<MagicAreaEntity,OceanFreezeState> states(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.TYPE);
	}

	public static Vec3 target(ServerPlayer owner, MagicAim aim) {
		if (!aim.valid()) return null;
		var level = (ServerLevel) owner.level();
		var from = owner.getEyePosition();
		var to = from.add(aim.direction().scale(96));
		if (!SpellRaycastAdapter.loaded(level, new AABB(from, to))) return null;
		var hit = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, owner));
		if (hit.getType() != HitResult.Type.BLOCK || !level.getBlockState(hit.getBlockPos()).is(Blocks.WATER)
				|| !level.getFluidState(hit.getBlockPos().above()).isEmpty()) return null;
		return Vec3.atBottomCenterOf(hit.getBlockPos().above());
	}

	public static boolean cast(ServerPlayer owner, MagicAim aim) {
		var point = target(owner, aim);
		if (point == null) return false;
		var area = new MagicAreaEntity(MagicSystem.MAGIC_AREA, owner.level());
		area.configure(owner, SpellDefinition.OCEAN_FREEZE, aim);
		area.setPos(point);
		if (!AreaRuntimeAdapter.replaceExpansion(owner, area)) return false;
		var state = new OceanFreezeState(owner.level().getGameTime(),-1,0);
		states(area).put(area, state);
		tick(area);
		return true;
	}

	public static double radius(long elapsed) { return OceanFreezeRules.radius(elapsed); }

	public static void tick(MagicAreaEntity area) {
		if (!(area.level() instanceof ServerLevel level)) return;
		var state = states(area).get(area);
		long elapsed = state == null ? 12000 : level.getGameTime() - state.start();
		if (OceanFreezeRules.expired(elapsed) || !(area.getOwner() instanceof ServerPlayer owner)
				|| !owner.isAlive() || owner.level() != level || !AreaRuntimeAdapter.adoptExpansion(owner, area)) {
			states(area).remove(area); area.discard(); return;
		}
		double radius = radius(elapsed);
		int edge = Math.min(64, (int) radius);
		if (OceanFreezeRules.scanDue(state.scanned(),edge,elapsed)) {
			state.addBlocks(TerrainLeaseRepository.freezeOcean(owner, area.position(), state.scanned(), edge,
					OceanFreezeRules.remainingBudget(state.blocks()), state.start() + 12000));
			state.scanned(edge);
		}
		if (OceanFreezeRules.damageActive(elapsed)) {
			for (var target : level.getEntitiesOfClass(LivingEntity.class,
					new AABB(area.position(), area.position()).inflate(radius + 1, 3, radius + 1),
					t -> MagicEffects.allowed(owner, t))) {
				var offset = target.getBoundingBox().getCenter().subtract(area.position());
				OceanFreezeHit.apply(new OceanFreezeHit.Target(){
					public boolean alreadyHit(){return state.hit(target.getUUID());}
					public boolean outside(){return offset.horizontalDistanceSqr()>radius*radius||Math.abs(offset.y)>3;}
					public boolean visible(){return SpellVisibilityAdapter.visible(level,area.position().add(0,.2,0),target,area);}
					public void claim(){state.claim(target.getUUID());}
					public boolean hurt(){return target.hurtServer(level,MagicDamage.source(area,area.spell()),20);}
					public boolean canFreeze(){return target.canFreeze();}
					public void freeze(){target.addEffect(new MobEffectInstance(MagicEffects.FROZEN,700,0),owner);}
				});
			}
		}
		if (elapsed % 5 != 0) return;
		boolean warning = elapsed >= 11800;
		int samples = warning ? 48 : 40;
		for (int i = 0; i < samples; i++) {
			double angle = i * Math.PI * 2 / samples + elapsed * .007;
			double r = warning ? 64 * Math.sqrt(level.getRandom().nextDouble()) : radius;
			var p = area.position().add(Math.cos(angle) * r, .12, Math.sin(angle) * r);
			if (!level.hasChunkAt(BlockPos.containing(p)) || !level.getBlockState(BlockPos.containing(p).below()).is(MagicTerrain.EXPANSION_ICE)) continue;
			if (warning) {
				level.sendParticles(new net.minecraft.core.particles.BlockParticleOption(ParticleTypes.BLOCK, MagicTerrain.EXPANSION_ICE.defaultBlockState()),
						p.x, p.y, p.z, 5, .5, .06, .5, .035);
			} else {
				level.sendParticles(ParticleTypes.SNOWFLAKE, p.x, p.y, p.z, 3, .7, .18, .7, .025);
				level.sendParticles(ParticleTypes.CLOUD, p.x, p.y + .18, p.z, 1, .4, .1, .4, .012);
			}
		}
	}

	public static void save(MagicAreaEntity area, ValueOutput out) {
		var s = states(area).get(area);
		if (s == null) return;
		out.putLong("OceanStart", s.start());
		out.putInt("OceanScanned", s.scanned());
		out.putInt("OceanBlocks", s.blocks());
		out.putString("OceanHit", s.savedHits());
	}

	public static void load(MagicAreaEntity area, ValueInput in) {
		var s = new OceanFreezeState(in.getLongOr("OceanStart",-12000),OceanFreezeRules.restoredScanned(in.getIntOr("OceanScanned",64)),OceanFreezeRules.restoredBlocks(in.getIntOr("OceanBlocks",8192)));
		s.restoreHits(in.getStringOr("OceanHit",""));
		states(area).put(area, s);
	}

	private OceanFreezeRuntimeAdapter() {}
}
