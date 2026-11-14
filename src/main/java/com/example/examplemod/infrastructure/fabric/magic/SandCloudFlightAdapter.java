package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
/** Sand-cloud projectile world sweep and conversion into a finite fog volume. */
public final class SandCloudFlightAdapter {
	public record Step(boolean flying,Vec3 velocity,long expiry,long next) {}
	public static Step advance(MagicAreaEntity area,Vec3 velocity,long expiry,long next,ServerLevel level) {
		var from=area.position();var to=from.add(velocity);
		if(from.y<level.getMinY()||!SpellRaycastAdapter.loaded(level,new AABB(from,to).inflate(.25))){area.discard();return new Step(true,velocity,expiry,next);}
		var hit=level.clip(new ClipContext(from,to,ClipContext.Block.COLLIDER,ClipContext.Fluid.ANY,area));
		Vec3 point=hit.getLocation();double distance=from.distanceToSqr(point);boolean contacted=hit.getType()!=HitResult.Type.MISS;
		for(var t:level.getEntitiesOfClass(LivingEntity.class,new AABB(from,point).inflate(.25),t->t!=area.getOwner()&&t.isAlive())){
			var intersection=t.getBoundingBox().inflate(.25).clip(from,point);
			if(intersection.isPresent()&&from.distanceToSqr(intersection.get())<distance){point=intersection.get();distance=from.distanceToSqr(point);contacted=true;}
		}
		if(contacted){area.setPos(point.subtract(velocity.normalize().scale(.03)));return new Step(false,velocity,level.getGameTime()+120,level.getGameTime());}
		else {area.setPos(to);velocity=velocity.add(0,-.05,0);ParticlePacketAdapter.send(level,SpellParticles.trail(area.spell()),to.x,to.y,to.z,5,.18,.18,.18,.02);}
		return new Step(true,velocity,expiry,next);
	}
	private SandCloudFlightAdapter() {}
}
