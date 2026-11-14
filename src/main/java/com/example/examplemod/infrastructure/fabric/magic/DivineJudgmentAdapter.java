package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.WindFieldRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.holy.divine_judge.*;

import java.util.WeakHashMap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** Three separately timed pulses; unloaded pulses are skipped rather than replayed. */
public final class DivineJudgmentAdapter {
	private static final class Runtime {
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,DivineJudgePulse>> TYPE =
		net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("divine_judgment_adapter_runtime"),
			builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,DivineJudgePulse>()));
	}

	/** The level owns this transient index; original explicit ValueInput/ValueOutput remains authoritative. */
	private static java.util.Map<MagicAreaEntity,DivineJudgePulse> states(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.TYPE);
	}

	public static boolean cast(ServerPlayer owner, MagicAim aim) {
		var spell = SpellDefinition.DIVINE_JUDGE;
		var point = WindFieldRuntimeAdapter.target(owner, spell, aim);
		if (point == null) return false;
		var area = new MagicAreaEntity(MagicSystem.MAGIC_AREA, owner.level());
		area.configure(owner, spell, aim);
		area.setPos(point);
		if (!AreaRuntimeAdapter.replaceExpansion(owner, area)) return false;
		states(area).put(area, new DivineJudgePulse(owner.level().getGameTime(), 0));
		tick(area);
		return true;
	}

	public static AABB bounds(Vec3 center) {
		return new AABB(center.add(-12, 0, -12), center.add(12, 32, 12));
	}

	public static int pulseIndex(long elapsed) {
		return DivineJudgeRules.pulseIndex(elapsed);
	}

	public static void tick(MagicAreaEntity area) {
		if (!(area.level() instanceof ServerLevel level)) return;
		DivineJudgeTick.tick(new DivineJudgeTick.World(){
			public DivineJudgePulse state(){return states(area).get(area);}
			public long now(){return level.getGameTime();}
			public boolean ownerPresent(){return area.getOwner() instanceof ServerPlayer;}
			private ServerPlayer owner(){return (ServerPlayer)area.getOwner();}
			public boolean alive(){return owner().isAlive();}
			public boolean sameLevel(){return owner().level()==level;}
			public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner(),area);}
			public void removeState(){states(area).remove(area);}
			public void discard(){area.discard();}
			public void commit(DivineJudgePulse pulse){states(area).put(area,pulse);}
			public void strike(int index){DivineJudgmentAdapter.strike(area,level,owner(),index);}
		});
	}
	private static void strike(MagicAreaEntity area,ServerLevel level,ServerPlayer owner,int index){
		var center = area.position();
		var source = new DamageSource(level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE)
				.getOrThrow(HolyInstantAdapter.DAMAGE), area, owner);
		for (var target : level.getEntitiesOfClass(LivingEntity.class, bounds(center), e -> HolyInstantAdapter.eligible(owner, e))) {
			var d = target.getBoundingBox().getCenter().subtract(center);
			if (DivineJudgeRules.inside(d.x,d.y,d.z)
					&& SpellVisibilityAdapter.visible(level, center, target, area)) {
				target.hurtServer(level, source, area.spell().damage());
			}
		}
		for (int h = 1; h < 32; h += 3) {
			ParticlePacketAdapter.send(level,ParticleTypes.END_ROD, center.x, center.y + h, center.z, 12, .55, 1, .55, .075);
		}
		for (int i = 0; i < 24; i++) {
			double angle = i * Math.PI / 12 + index * .12;
			ParticlePacketAdapter.send(level,ParticleTypes.ELECTRIC_SPARK, center.x + Math.cos(angle) * 12, center.y + .2,
					center.z + Math.sin(angle) * 12, 5, .23, .3, .23, .025);
		}
		ParticlePacketAdapter.send(level,ParticleTypes.END_ROD, center.x, center.y + .35, center.z, 90, 4, .4, 4, .15);
	}

	public static void save(MagicAreaEntity area, ValueOutput out) {
		var pulse = states(area).get(area);
		if (pulse != null) { out.putLong("JudgmentStart", pulse.start()); out.putInt("JudgmentCount", pulse.count()); }
	}

	public static void load(MagicAreaEntity area, ValueInput in) {
		long start = in.getLongOr("JudgmentStart", -60);
		int count = DivineJudgeRules.restoredCount(in.getIntOr("JudgmentCount", 3));
		states(area).put(area, new DivineJudgePulse(start, count));
	}

	private DivineJudgmentAdapter() {}
}
