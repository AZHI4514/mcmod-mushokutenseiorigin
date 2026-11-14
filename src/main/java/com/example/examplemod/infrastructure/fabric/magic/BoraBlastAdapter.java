package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.mixed.bora_blast.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.*;

/** Cold pressure wave with post-damage warmth and wetness checks. */
public final class BoraBlastAdapter implements SelfEffect {
	private final ServerPlayer owner;
	public BoraBlastAdapter(ServerPlayer owner) {this.owner=owner;}
	public boolean apply(float yaw,float pitch) {return cast(owner,new MagicAim(yaw,pitch));}
	public static boolean cast(ServerPlayer owner,MagicAim aim) {
		var level=(ServerLevel)owner.level();var from=owner.getEyePosition();
		var source=new MagicAreaEntity(MagicSystem.MAGIC_AREA,level);
		source.configure(owner,SpellDefinition.BORA_BLAST,aim);source.setPos(from);
		for(var entity:level.getEntities(owner,new AABB(from,from).inflate(SpellDefinition.BORA_BLAST.range()),e->e instanceof LivingEntity&&MagicEffects.allowed(owner,(LivingEntity)e))) {
			var target=(LivingEntity)entity;var offset=target.getBoundingBox().getCenter().subtract(from);
			var direction=aim.direction();
			if(!BoraBlastRules.contains(offset.x,offset.y,offset.z,direction.x,direction.y,direction.z)||!SpellVisibilityAdapter.visible(level,from,target,source)) continue;
			BoraBlastHit.apply(new BoraBlastHit.Target() {
				public boolean hurt() {return target.hurtServer(level,MagicDamage.source(source,SpellDefinition.BORA_BLAST),SpellDefinition.BORA_BLAST.damage());}
				public boolean impulseImmune() {return MagicControl.immune(target);}
				public void impulse(double strength,boolean useAim) {
					target.setDeltaMovement(target.getDeltaMovement().add((useAim?aim.direction():offset.normalize()).scale(strength)));
					target.hurtMarked=true;
				}
				public boolean warm() {return target.hasEffect(MagicEffects.WARMTH);}
				public void removeWarm() {target.removeEffect(MagicEffects.WARMTH);}
				public boolean canFreeze() {return target.canFreeze();}
				public void freeze(int ticks) {target.addEffect(new MobEffectInstance(MagicEffects.FROZEN,ticks,0),owner);}
				public boolean wet() {return target.hasEffect(MagicEffects.WET);}
				public void slow(int ticks,int amplifier) {target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,ticks,amplifier),owner);}
				public void afterHit() {SpellHooks.afterHit(source,target);}
			},offset.lengthSqr());
		}
		SpellVisuals.burst(level,SpellDefinition.BORA_BLAST,from,aim.direction());return true;
	}
}
