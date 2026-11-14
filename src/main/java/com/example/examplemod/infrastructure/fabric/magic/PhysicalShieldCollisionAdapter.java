package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;

import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldSelection;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.*;

/** Fabric/world collision adaptation; core selection and impact rules own the decisions. */
public final class PhysicalShieldCollisionAdapter {
	public static void initialize() {
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(PhysicalShieldCollisionAdapter::allowDamage);
	}
	public static boolean allowDamage(LivingEntity target,DamageSource source,float amount) {

			if(source.getDirectEntity() instanceof Projectile projectile&&AreaRuntimeAdapter.isMagic(projectile)) return true;
			if(!(source.is(DamageTypeTags.IS_PROJECTILE)||source.getDirectEntity() instanceof LivingEntity)) return true;
			var origin=source.getSourcePosition(); if(origin==null) return true;
			var selected=PhysicalShieldSelection.firstIntersecting(PhysicalShieldRuntimeAdapter.shields((ServerLevel)target.level()).values(),
					shield->!shield.isRemoved()&&shield.level()==target.level(),shield->{
						var delta=target.getBoundingBox().getCenter().subtract(origin);double distance=delta.length();
						if(distance<1.0E-6)return Double.POSITIVE_INFINITY;
						return shield.volume().contact(new AABB(origin,origin),shield.center(),delta.normalize().scale(-1),distance);
					});
			if(selected!=null){selected.absorb(amount);return false;}
			return true;
	}
	public static boolean intercept(Projectile projectile) {
		if(!(projectile.level() instanceof ServerLevel serverLevel))return false;
		if(AreaRuntimeAdapter.isMagic(projectile)||projectile.isRemoved()||projectile.getDeltaMovement().lengthSqr()<1.0E-8) return false;
		var motion=projectile.getDeltaMovement();double length=motion.length();
		var selected=PhysicalShieldSelection.nearest(PhysicalShieldRuntimeAdapter.shields(serverLevel).values(),
				shield->!(shield.isRemoved()||shield.level()!=projectile.level()||shield.getOwner()==projectile.getOwner()||motion.dot(shield.direction())>=0),
				shield->shield.volume().contact(projectile.getBoundingBox(),shield.center(),motion.normalize().scale(-1),length));
		if(selected==null)return false;
		var end=projectile.position().add(motion.normalize().scale(selected.distance()));
		var shield=selected.shield();
		return com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldInterception.apply(new com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldInterception.Contact() {
			public boolean terrainClear(){return projectile.level().clip(new net.minecraft.world.level.ClipContext(projectile.position(),end,net.minecraft.world.level.ClipContext.Block.COLLIDER,net.minecraft.world.level.ClipContext.Fluid.NONE,projectile)).getType()==HitResult.Type.MISS;}
			public float incomingDamage(){return MagicProjectileDamage.incoming(projectile,shield);}
			public void absorb(float damage){shield.absorb(damage);}
			public boolean trident(){return projectile instanceof net.minecraft.world.entity.projectile.arrow.ThrownTrident;}
			public void rebound(){projectile.setPos(end.subtract(motion.normalize().scale(0.1)));projectile.setDeltaMovement(motion.scale(-0.1));}
			public void discard(){projectile.discard();}
		});
	}
	public static Vec3 clipShieldMovement(Entity entity,Vec3 movement) {
		double distance=movement.length(); if(distance<1.0E-8||entity instanceof Projectile||entity.isSpectator()) return movement;
		double first=distance;
		for(var other:entity.level().getEntities(entity,entity.getBoundingBox().expandTowards(movement).inflate(0.1),e->e instanceof DirectionalSpellCarrier p&&p.shield())) {
			var shield=(DirectionalSpellCarrier)other;
			// Let entities already intersecting the surface escape rather than trapping them.
			if(shield.volume().intersects(entity.getBoundingBox(),shield.center())) {
				double side=entity.getBoundingBox().getCenter().subtract(shield.center()).dot(shield.direction());
				var resolved=com.example.examplemod.infrastructure.fabric.magic.PhysicalShieldCollisionAdapter.shieldOverlapMotion(movement,shield.direction(),side);
				if(resolved.equals(movement))continue;return resolved;
			}
			first=Math.min(first,shield.volume().contact(entity.getBoundingBox(),shield.center(),movement.normalize().scale(-1),distance));
		}
		return com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldMovement.clips(first,distance)
				?movement.scale(com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldMovement.scale(first,distance)):movement;
	}
	private PhysicalShieldCollisionAdapter() { }
	public static Vec3 shieldOverlapMotion(Vec3 movement,Vec3 normal,double side) {
		double toward=movement.dot(normal);
		if(com.example.examplemod.magic.spell.barrier.physical_shield.PhysicalShieldMovement.canEscape(side,toward))return movement;
		return movement.subtract(normal.scale(toward));
	}

}
