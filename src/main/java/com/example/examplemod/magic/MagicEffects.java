package com.example.examplemod.magic;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/** 湿身无额外效果；冻结计时只维持原版冻结值，伤害和减速仍由原版执行。 */
public final class MagicEffects {
	public static final Holder<MobEffect> WIND_BOUND = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ExampleMod.id("wind_bound"), new WindBound());
	public static final Holder<MobEffect> ICE_BOUND = register("ice_bound", MobEffectCategory.HARMFUL, 0xA8E9F7);
	public static final Holder<MobEffect> ARC_BOUND = register("arc_bound", MobEffectCategory.HARMFUL, 0xB4A0FF);
	public static final Holder<MobEffect> MUD_HELD = register("mud_held", MobEffectCategory.HARMFUL, 0x564A35);
	public static final Holder<MobEffect> MUD_RAIN_HELD = register("mud_rain_held", MobEffectCategory.HARMFUL, 0x806E4D);
	public static final Holder<MobEffect> SEALED = register("sealed", MobEffectCategory.HARMFUL, 0xC7B6FF);
	public static final Holder<MobEffect> HYPNOTIZED = register("hypnotized", MobEffectCategory.HARMFUL, 0xB494DD);
	public static final Holder<MobEffect> HEAVY_GRAVITY = register("heavy_gravity", MobEffectCategory.HARMFUL, 0x7E6A9F);
	public static final Holder<MobEffect> WET = register("wet", MobEffectCategory.NEUTRAL, 0x397DCE);
	public static final Holder<MobEffect> WARMTH = register("warmth", MobEffectCategory.BENEFICIAL, 0xEBA654);
	public static final Holder<MobEffect> FROZEN = register("magic_frozen", MobEffectCategory.HARMFUL, 0xB0E7FF);
	private static Holder<MobEffect> register(String id, MobEffectCategory category, int color) {
		return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ExampleMod.id(id), new Marker(category, color));
	}
	private static final class WindBound extends MobEffect {
		WindBound() { super(MobEffectCategory.HARMFUL,0xB9E4D8); }
		@Override public boolean shouldApplyEffectTickThisTick(int remaining,int amplifier) { return com.example.examplemod.magic.spell.wind.wind_bind.WindBindSpec.pulse(remaining); }
		@Override public boolean applyEffectTick(net.minecraft.server.level.ServerLevel level,LivingEntity target,int amplifier) { target.hurtServer(level,target.damageSources().magic(),com.example.examplemod.magic.spell.wind.wind_bind.WindBindSpec.PULSE_DAMAGE); return true; }
	}
	private static final class Marker extends MobEffect {
		Marker(MobEffectCategory category, int color) { super(category, color); }
	}
	public static void initialize() { }
	public static boolean allowed(Entity owner, LivingEntity target) {
		return target != owner && target.isAlive() && !target.isSpectator()
				&& (!(owner instanceof Player player) || !(target instanceof Player other) || player.canHarmPlayer(other));
	}
	public static void apply(Entity owner, Entity victim, SpellDefinition spell) {
		if (!(victim instanceof LivingEntity target) || !allowed(owner, target)) return;
		float roll = target.getRandom().nextFloat();
		if (spell == SpellDefinition.FIREBALL) {
			int ticks = com.example.examplemod.magic.spell.fire.fireball.FireballImpactPolicy.ignitionTicks(roll,
					target.fireImmune(), target.hasEffect(net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE));
			if (ticks > 0) target.igniteForTicks(ticks);
			return;
		}
		if (spell == SpellDefinition.WATER_ARROW) {
			int ticks = com.example.examplemod.magic.spell.water.water_arrow.WaterArrowImpactPolicy.statusTicks(roll);
			if (ticks > 0) target.addEffect(new MobEffectInstance(WET, ticks, 0), owner);
			return;
		}
		if (spell == SpellDefinition.ICE_SMASH) {
			int ticks = com.example.examplemod.magic.spell.water.ice_smash.IceSmashImpactPolicy.statusTicks(roll, target.canFreeze(), target.hasEffect(WARMTH));
			if (ticks > 0) target.addEffect(new MobEffectInstance(FROZEN, ticks, 0), owner);
			return;
		}
		if (spell == SpellDefinition.FLAME_SLICE) {
			int ticks = com.example.examplemod.magic.spell.fire.flame_slice.FlameSliceRules.ignitionTicks(roll,
					target.fireImmune(), target.hasEffect(net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE));
			if (ticks > 0) target.igniteForTicks(ticks);
			return;
		}
		if (spell == SpellDefinition.ICE_BLADE) {
			int ticks = com.example.examplemod.magic.spell.water.ice_blade.IceBladeImpactPolicy.statusTicks(roll,target.canFreeze(),target.hasEffect(WARMTH));
			if (ticks > 0) target.addEffect(new MobEffectInstance(FROZEN,ticks,0),owner);
			return;
		}
		if (spell == SpellDefinition.WATER_CANNON) {
			int ticks = com.example.examplemod.magic.spell.water.water_cannon.WaterCannonRules.wetTicks(roll);
			if(ticks>0)target.addEffect(new MobEffectInstance(WET,ticks,0),owner);
			return;
		}
		if(spell==SpellDefinition.EX_FLAME) {
			int ticks=com.example.examplemod.magic.spell.fire.ex_flame.ExFlameRules.ignitionTicks(roll,target.fireImmune(),target.hasEffect(net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE));
			if(ticks>0)target.igniteForTicks(ticks);return;
		}
		if(spell==SpellDefinition.ICE_PILLAR) {
			int ticks=com.example.examplemod.magic.spell.water.ice_pillar.IcePillarRules.freezeTicks(roll,target.canFreeze(),target.hasEffect(WARMTH));
			if(ticks>0)target.addEffect(new MobEffectInstance(FROZEN,ticks,0),owner);
			return;
		}
		if(spell==SpellDefinition.ICE_LANCE) {
			int ticks=com.example.examplemod.magic.spell.water.ice_lance.IceLanceRules.freezeTicks(roll,target.canFreeze(),target.hasEffect(WARMTH));
			if(ticks>0)target.addEffect(new MobEffectInstance(FROZEN,ticks,0),owner);
			return;
		}
		if(spell==SpellDefinition.ICICLE_BREAK) {
			int ticks=com.example.examplemod.magic.spell.water.icicle_break.IcicleBreakSpec.freezeTicks(roll,target.canFreeze(),target.hasEffect(WARMTH));
			if(ticks>0)target.addEffect(new MobEffectInstance(FROZEN,ticks,0),owner);
			return;
		}
		if(spell==SpellDefinition.ICICLE_EDGE) {
			int ticks=com.example.examplemod.magic.spell.water.icicle_edge.IcicleEdgeSpec.freezeTicks(roll,target.canFreeze(),target.hasEffect(WARMTH));
			if(ticks>0)target.addEffect(new MobEffectInstance(FROZEN,ticks,0),owner);
			return;
		}
		if(spell==SpellDefinition.FLOOD_FLUSH) {
			int ticks=com.example.examplemod.magic.spell.water.flood_flush.FloodFlushSpec.wetTicks(roll);
			if(ticks>0)target.addEffect(new MobEffectInstance(WET,ticks,0),owner);
			return;
		}
		if (spell.icy()) {
			if (roll < 0.8F && target.canFreeze() && !target.hasEffect(WARMTH))
				target.addEffect(new MobEffectInstance(FROZEN, spell.statusTicks(), 0), owner);
		} else if (spell.element().equals("water")) {
			if (roll < 0.6F) target.addEffect(new MobEffectInstance(WET, spell.statusTicks(), 0), owner);
		} else if (spell.element().equals("fire") && roll < 0.8F && !target.fireImmune()
				&& !target.hasEffect(net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE)) target.igniteForTicks(spell.statusTicks());
	}
	public static void warm(LivingEntity target, int ticks) {
		target.removeEffect(FROZEN);
		target.setTicksFrozen(0);
		target.addEffect(new MobEffectInstance(WARMTH, ticks, 0));
	}
	/** 在原版aiStep的冻结结算前运行，让原版处理减速、皮革防护和每40tick冻伤。 */
	public static void beforeFreeze(LivingEntity target) {
		boolean arc=target.isAlive()&&target.hasEffect(ARC_BOUND);
		if(target.getAttachedOrElse(MagicAttachments.ARC_BOUND_VISUAL,false)!=arc)target.setAttached(MagicAttachments.ARC_BOUND_VISUAL,arc);
		boolean bound=target.isAlive() && target.hasEffect(WIND_BOUND);
		if(target.getAttachedOrElse(MagicAttachments.WIND_BOUND_VISUAL,false)!=bound)target.setAttached(MagicAttachments.WIND_BOUND_VISUAL,bound);
		if (target.hasEffect(WARMTH)) {
			target.setTicksFrozen(0);
			target.removeEffect(FROZEN);
		} else if (target.hasEffect(FROZEN)) {
			if (target.getEffect(FROZEN).getDuration() <= 1 || !target.canFreeze()) target.setTicksFrozen(0);
			else target.setTicksFrozen(target.getTicksRequiredToFreeze() + 2);
		}
	}
	private MagicEffects() { }
}
