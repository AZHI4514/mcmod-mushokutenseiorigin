package com.example.examplemod.magic;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;

/** New hard-control resistance is independent of existing wind binding. */
public final class MagicControl {
	public static final net.minecraft.tags.TagKey<net.minecraft.world.entity.EntityType<?>> BIND_IMMUNE=
		net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE,com.example.examplemod.ExampleMod.id("wind_bind_immune"));
	public static boolean immune(LivingEntity target) { return target.getType().builtInRegistryHolder().is(MagicControl.BIND_IMMUNE); }
	public static int duration(int desired,boolean player,long now,long lastEnd) {
		int ticks=Math.max(0,Math.min(desired,player?40:Integer.MAX_VALUE));
		return ticks==0?0:now<lastEnd+60?Math.max(5,ticks/2):ticks;
	}
	public static boolean apply(LivingEntity target,int ticks) { return apply(target,ticks,MagicEffects.ARC_BOUND); }
	public static boolean apply(LivingEntity target,int ticks,net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect) {
		if(immune(target))return false;
		long now=target.level().getGameTime(),last=target.getAttachedOrElse(MagicAttachments.CONTROL_END,Long.MIN_VALUE/2);
		int duration=duration(ticks,target instanceof Player,now,last);if(duration==0)return false;
		if(target.hasEffect(MagicEffects.ARC_BOUND)||target.hasEffect(MagicEffects.ICE_BOUND))return false;
		if(!target.addEffect(new MobEffectInstance(effect,duration,0)))return false;
		target.setAttached(MagicAttachments.CONTROL_END,now+duration);return true;
	}
	private MagicControl(){}
	public static net.minecraft.world.phys.Vec3 boundMovement(net.minecraft.world.phys.Vec3 movement){return net.minecraft.world.phys.Vec3.ZERO;}
}
