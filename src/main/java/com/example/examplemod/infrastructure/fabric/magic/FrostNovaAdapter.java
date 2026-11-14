package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;

import net.minecraft.server.level.*;
import com.example.examplemod.magic.spell.mixed.frost_nova.FrostNovaRules;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** One release, one damage attempt; warmth protects control, not damage. */
public final class FrostNovaAdapter implements SelfEffect {
	private final ServerPlayer owner;
	public FrostNovaAdapter(ServerPlayer owner){this.owner=owner;}
	public boolean apply(float yaw,float pitch){return cast(owner,new MagicAim(yaw,pitch));}
	public static boolean cast(ServerPlayer owner,MagicAim aim){
		var level=(ServerLevel)owner.level();var center=owner.position().add(0,1,0);
		var source=new MagicAreaEntity(MagicSystem.MAGIC_AREA,level);source.configure(owner,SpellDefinition.FROST_NOVA,aim);source.setPos(center);
		for(var entity:level.getEntities(owner,new AABB(center,center).inflate(FrostNovaRules.RADIUS),e->e instanceof LivingEntity&&MagicEffects.allowed(owner,(LivingEntity)e))){
			var target=(LivingEntity)entity;if(!FrostNovaRules.inside(target.getBoundingBox().getCenter().distanceToSqr(center))||!SpellVisibilityAdapter.visible(level,center,target,source))continue;
			com.example.examplemod.magic.spell.mixed.frost_nova.FrostNovaHit.apply(new com.example.examplemod.magic.spell.mixed.frost_nova.FrostNovaHit.Target() {
				public boolean wet(){return target.hasEffect(MagicEffects.WET);}
				public boolean warm(){return target.hasEffect(MagicEffects.WARMTH);}
				public boolean hurt(float damage){return target.hurtServer(level,MagicDamage.source(source,SpellDefinition.FROST_NOVA),damage);}
				public void removeWet(){target.removeEffect(MagicEffects.WET);}
				public void removeWarm(){target.removeEffect(MagicEffects.WARMTH);}
				public boolean canFreeze(){return target.canFreeze();}
				public void freeze(int ticks){target.addEffect(new MobEffectInstance(MagicEffects.FROZEN,ticks,0),owner);}
				public boolean player(){return target instanceof Player;}
				public void control(int ticks){MagicControl.apply(target,ticks,MagicEffects.ICE_BOUND);}
				public void afterHit(){SpellHooks.afterHit(source,target);}
			});
		}SpellVisuals.burst(level,SpellDefinition.FROST_NOVA,center,new Vec3(0,0,1));return true;
	}

}
