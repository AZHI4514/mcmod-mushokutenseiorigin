package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.*;
import net.minecraft.world.level.ClipContext;

public final class SpellRaycastAdapter {
	public record Ray(Vec3 point,BlockPos base,LivingEntity target) {}
	public static BlockHitResult aim(ServerPlayer player,MagicAim aim) {
		var from=player.getEyePosition(); var end=from.add(aim.direction().scale(12));
		var level=(ServerLevel)player.level();
		if(!loaded(level,new AABB(from,end).inflate(0.01))) return null;
		return level.clip(new ClipContext(from,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,player));
	}
	public static boolean loaded(ServerLevel level,AABB box) {
		return com.example.examplemod.magic.mechanic.projectile.SweptChunkGate.available(box.minX,box.maxX,box.minZ,box.maxZ,(x,z)->level.hasChunkAt(new BlockPos(x,0,z)));
	}
	public static LivingEntity aimedEntity(ServerPlayer player,MagicAim direction) {
		var hit=aim(player,direction); if(hit==null) return null;
		var from=player.getEyePosition(); var to=hit.getLocation(); double nearest=from.distanceTo(to); LivingEntity selected=null;
		for(var candidate:player.level().getEntities(player,new AABB(from,to).inflate(1),e->e instanceof LivingEntity && MagicEffects.allowed(player,(LivingEntity)e))) {
			var intersection=candidate.getBoundingBox().inflate(0.1).clip(from,to);
			if(intersection.isPresent() && intersection.get().distanceTo(from)<nearest) { nearest=intersection.get().distanceTo(from); selected=(LivingEntity)candidate; }
		}
		return selected;
	}
	public static Ray trace(ServerPlayer player,MagicAim aim,double range) {
		return traceFrom(player,player.getEyePosition(),aim.direction(),range);
	}
	public static Ray traceFrom(ServerPlayer player,Vec3 from,Vec3 direction,double range) {
		var level=(ServerLevel)player.level();var end=from.add(direction.scale(range));
		if(!loaded(level,new AABB(from,end).inflate(1))) return null;
		var block=level.clip(new ClipContext(from,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,player));
		Vec3 point=block.getLocation(); LivingEntity target=null; double closest=from.distanceToSqr(point);
		for(var candidate:level.getEntities(player,new AABB(from,point).inflate(1),e->e instanceof LivingEntity&&MagicEffects.allowed(player,(LivingEntity)e))) {
			var intersect=candidate.getBoundingBox().clip(from,point);
			if(intersect.isPresent()&&from.distanceToSqr(intersect.get())<=closest) { point=intersect.get();closest=from.distanceToSqr(point);target=(LivingEntity)candidate; }
		}
		if(target!=null) return new Ray(point,BlockPos.containing(target.getX(),target.getBoundingBox().minY,target.getZ()),target);
		if(block.getType()!=HitResult.Type.BLOCK) return null;
		var base=block.getBlockPos().relative(block.getDirection());
		return new Ray(point,base,null);
	}
	private SpellRaycastAdapter() { }
}
