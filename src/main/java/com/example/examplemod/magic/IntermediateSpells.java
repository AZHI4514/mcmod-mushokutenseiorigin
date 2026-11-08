package com.example.examplemod.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;

public final class IntermediateSpells {
	public static IntermediateProjectile projectile(ServerPlayer owner,SpellDefinition spell,MagicAim aim,int paid) {
		var projectile=new IntermediateProjectile(MagicSystem.intermediateType(spell),owner.level());
		projectile.setOwner(owner); projectile.paidMana(paid); projectile.setPos(owner.getEyePosition());
		var direction=aim.direction(); projectile.shoot(direction.x,direction.y,direction.z,spell.speed(),0); return projectile;
	}
	public static boolean execute(ServerPlayer owner,SpellDefinition spell,MagicAim aim,int paid) {
		var projectile=projectile(owner,spell,aim,paid);
		if(spell.execution()==SpellDefinition.Execution.ADVANCED_PROJECTILE) return owner.level().addFreshEntity(projectile);
		var level=(ServerLevel)owner.level(); var from=owner.getEyePosition(); var end=from.add(aim.direction().scale(12));
		for(int step=0;step<=12;step++)
			if(!level.hasChunkAt(BlockPos.containing(from.add(aim.direction().scale(step))))) return false;
		var hit=level.clip(new ClipContext(from,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,owner));
		var center=hit.getType()==HitResult.Type.MISS?end:hit.getLocation().subtract(aim.direction().scale(0.02)); projectile.setPos(center);
		var range=new AABB(center.x-6,center.y-6,center.z-6,center.x+6,center.y+6,center.z+6);
		// 收集全部结果后才造冰，避免第一个目标的冰块遮住同次施法的其他目标。
		var encased=new java.util.ArrayList<LivingEntity>();
		for(var entity:level.getEntities(owner,range,e->e instanceof LivingEntity&&MagicEffects.allowed(owner,(LivingEntity)e))) {
			var target=(LivingEntity)entity;
			if(target.getBoundingBox().getCenter().distanceToSqr(center)>36||!visible(level,center,target,owner)) continue;
			boolean wet=target.hasEffect(MagicEffects.WET);
			if(target.hurtServer(level,MagicDamage.source(projectile,spell),spell.damage())) {
				if(spell==SpellDefinition.WATER_SPLASH) target.addEffect(new MobEffectInstance(MagicEffects.WET,spell.statusTicks(),0),owner);
				else {
					MagicEffects.apply(owner,target,spell);
					if(wet) { target.removeEffect(MagicEffects.WET); encased.add(target); }
				}
				SpellHooks.afterHit.accept(projectile,target);
			}
		}
		encased.forEach(target->MagicTerrain.encase(owner,target));
		SpellVisuals.burst(level,spell,center,aim.direction());
		return true;
	}
	public static void hurt(IntermediateProjectile projectile,LivingEntity target,SpellDefinition spell,float damage) {
		var level=(ServerLevel)projectile.level();
		if(!target.hurtServer(level,MagicDamage.source(projectile,spell),damage)) return;
		if(spell.knockback()>0) { var motion=projectile.getDeltaMovement(); target.knockback(spell.knockback(),-motion.x,-motion.z); }
		MagicEffects.apply(projectile.getOwner(),target,spell); SpellHooks.afterHit.accept(projectile,target);
	}
	public static void blockImpact(IntermediateProjectile projectile,ServerPlayer owner,BlockPos block,Vec3 center) {
		var level=(ServerLevel)projectile.level();
		// 实体群伤与地形各结算一次；从接触前的空气侧检查遮挡。
		var visibleFrom=center.subtract(projectile.getDeltaMovement().normalize().scale(0.02));
		for(var entity:level.getEntities(projectile,IntermediateRules.impactBounds(center),e->e instanceof LivingEntity&&MagicEffects.allowed(owner,(LivingEntity)e)))
			if(visible(level,visibleFrom,(LivingEntity)entity,projectile)) hurt(projectile,(LivingEntity)entity,projectile.spell(),projectile.damage());
		MagicTerrain.blast(owner,block,center,projectile.spell()==SpellDefinition.WATER_CANNON?1:2,projectile.spell()==SpellDefinition.EX_FLAME);
	}
	public static boolean visible(ServerLevel level,Vec3 from,LivingEntity target,net.minecraft.world.entity.Entity source) {
		var to=target.getBoundingBox().getCenter(); var difference=to.subtract(from);
		int steps=Math.max(1,(int)Math.ceil(difference.length()));
		for(int i=0;i<=steps;i++)
			if(!level.hasChunkAt(BlockPos.containing(from.add(difference.scale(i/(double)steps))))) return false;
		return level.clip(new ClipContext(from,to,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,source)).getType()==HitResult.Type.MISS;
	}
	public static void impactParticles(IntermediateProjectile projectile) {
		var level=(ServerLevel)projectile.level();
		SpellVisuals.burst(level,projectile.spell(),projectile.position(),projectile.getDeltaMovement().normalize());
		if(projectile.getOwner() instanceof ServerPlayer owner) MagicSounds.listener.accept(owner,MagicSounds.Phase.IMPACT);
	}
	private IntermediateSpells() { }
}
