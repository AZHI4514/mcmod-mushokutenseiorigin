package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.mixed.blast.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.*;

/** Hot pressure wave without ignition, terrain changes or caster recoil. */
public final class BlastAdapter implements SelfEffect {
	private final ServerPlayer owner;
	public BlastAdapter(ServerPlayer owner) {this.owner=owner;}
	public boolean apply(float yaw,float pitch) {return cast(owner,new MagicAim(yaw,pitch));}
	public static boolean cast(ServerPlayer owner,MagicAim aim) {
		var level=(ServerLevel)owner.level();var from=owner.getEyePosition();
		var source=new MagicAreaEntity(MagicSystem.MAGIC_AREA,level);
		source.configure(owner,SpellDefinition.BLAST,aim);source.setPos(from);
		for(var entity:level.getEntities(owner,new AABB(from,from).inflate(SpellDefinition.BLAST.range()),e->e instanceof LivingEntity&&MagicEffects.allowed(owner,(LivingEntity)e))) {
			var target=(LivingEntity)entity;var offset=target.getBoundingBox().getCenter().subtract(from);
			var direction=aim.direction();
			if(!BlastRules.contains(offset.x,offset.y,offset.z,direction.x,direction.y,direction.z)||!SpellVisibilityAdapter.visible(level,from,target,source)) continue;
			BlastHit.apply(new BlastHit.Target() {
				public boolean hurt() {return target.hurtServer(level,MagicDamage.source(source,SpellDefinition.BLAST),SpellDefinition.BLAST.damage());}
				public boolean impulseImmune() {return MagicControl.immune(target);}
				public void impulse(double strength,boolean useAim) {
					target.setDeltaMovement(target.getDeltaMovement().add((useAim?aim.direction():offset.normalize()).scale(strength)));
					target.hurtMarked=true;
				}
				public void afterHit() {SpellHooks.afterHit(source,target);}
			},offset.lengthSqr());
		}
		SpellVisuals.burst(level,SpellDefinition.BLAST,from,aim.direction());return true;
	}
}
