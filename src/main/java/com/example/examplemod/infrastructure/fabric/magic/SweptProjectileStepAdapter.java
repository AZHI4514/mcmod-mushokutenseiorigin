package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.bootstrap.ProjectileHitComposition;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.projectile.ProjectileServerStep;
import java.util.*;
import java.util.function.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.*;
/** One sweep adapter; carrier fields remain private. */
public final class SweptProjectileStepAdapter implements ProjectileServerStep.World {
	public static void prepareAndApply(SweptSpellProjectile projectile,ServerLevel level,int age,Set<UUID> hit,Runnable advanceAge,IntSupplier broken,Runnable incrementBroken,DoubleConsumer travel) {
		var policy=com.example.examplemod.magic.mechanic.projectile.SweptProjectilePolicy.of(projectile.spell());
		if(projectile.getY()<level.getMinY()||policy.expiry().expired(age,projectile.spell().flightTicks(),projectile.getDeltaMovement().lengthSqr())){projectile.discard();return;}
		var from=projectile.position(); var direction=projectile.getDeltaMovement().normalize();
		double distance=projectile.spell().speed();
		var body=new SpellSweep(direction,policy.width(),policy.height(),policy.depth());
		var to=from.add(direction.scale(distance)); var bounds=body.bounds(from).minmax(body.bounds(to)).inflate(1.0E-5);
		ProjectileServerStep.apply(new SweptProjectileStepAdapter(projectile,level,body,from,direction,distance,to,bounds,hit,advanceAge,broken,incrementBroken,travel));
	}

 private com.example.examplemod.magic.mechanic.projectile.SweptProjectilePolicy policy(){return com.example.examplemod.magic.mechanic.projectile.SweptProjectilePolicy.of(projectile.spell());}
 private final SweptSpellProjectile projectile;
 private final ServerLevel level;
 private final SpellSweep body;
 private final Vec3 from,direction,to;
 private final double distance;
 private final AABB bounds;
 private final Set<UUID> hit;
 private final Runnable advanceAge,incrementBroken;
 private final IntSupplier broken;
 private final DoubleConsumer travel;
 private SweptProjectileStepAdapter(SweptSpellProjectile projectile,ServerLevel level,SpellSweep body,Vec3 from,Vec3 direction,double distance,Vec3 to,AABB bounds,Set<UUID> hit,Runnable advanceAge,IntSupplier broken,Runnable incrementBroken,DoubleConsumer travel) {
  this.projectile=projectile;this.level=level;this.body=body;this.from=from;this.direction=direction;this.distance=distance;this.to=to;this.bounds=bounds;this.hit=hit;this.advanceAge=advanceAge;this.broken=broken;this.incrementBroken=incrementBroken;this.travel=travel;
 }

		private double wall,fluid;
		private BlockPos block;
		private Vec3 impact;
		public boolean loaded() {
			return com.example.examplemod.magic.mechanic.projectile.SweptChunkGate.available(bounds.minX,bounds.maxX,bounds.minZ,bounds.maxZ,
					(x,z)->level.hasChunkAt(new BlockPos(x,projectile.getBlockY(),z)));
		}
		public void unloaded() {if(policy().discardUnloaded())projectile.discard();}
		public void advanceAge() {advanceAge.run();}
		public void solidsAndTerrain() {
			var solids=new com.example.examplemod.magic.mechanic.projectile.SolidContacts<BlockPos,Vec3>();
			for(var pos:BlockPos.betweenClosed(BlockPos.containing(bounds.minX,bounds.minY,bounds.minZ),BlockPos.containing(bounds.maxX,bounds.maxY,bounds.maxZ))) {
				if(MagicFlight.passesWaterBlock(projectile,pos)) continue;
				for(var local:level.getBlockState(pos).getCollisionShape(level,pos).toAabbs()) {
					double contact=body.contact(local.move(pos),from,direction,distance);
					solids.accept(pos.immutable(),contact,distance,()->{
						var box=local.move(pos);var center=from.add(direction.scale(contact));
						return new Vec3(Math.clamp(center.x,box.minX,box.maxX),Math.clamp(center.y,box.minY,box.maxY),Math.clamp(center.z,box.minZ,box.maxZ));
					});
				}
			}
			wall=solids.wall();block=solids.block();impact=solids.impact();
			var contacts=solids.contacts();
			if(policy().harvestsTerrain()&&MagicConfig.permanentTerrain()&&projectile.getOwner() instanceof ServerPlayer owner){
				var ordered=new ArrayList<>(contacts.entrySet());ordered.sort(java.util.Map.Entry.comparingByValue());
				wall=Double.POSITIVE_INFINITY;block=null;impact=null;
				var obstacle=com.example.examplemod.magic.spell.wind.tornado_impact.TornadoImpactTerrain.firstObstacle(ordered,
						new com.example.examplemod.magic.spell.wind.tornado_impact.TornadoImpactTerrain.World<java.util.Map.Entry<BlockPos,Double>>() {
							public int broken(){return broken.getAsInt();}
							public boolean breakBlock(java.util.Map.Entry<BlockPos,Double> contact){return PermanentTerrainAdapter.breakStoneHarvestable(owner,contact.getKey());}
							public void incrementBroken(){incrementBroken.run();}
						});
				if(obstacle!=null){wall=obstacle.getValue();block=obstacle.getKey();impact=from.add(direction.scale(wall));}
			}
		}
		public void fluids() {fluid=MagicFlight.fluidHit(projectile,body,from,direction.scale(distance));}
		public boolean targets() {
			final double candidateWall=wall;
			var targets=com.example.examplemod.magic.mechanic.projectile.ContactCandidates.collect(
					level.getEntities(projectile,bounds,e->e instanceof LivingEntity && MagicEffects.allowed(projectile.getOwner(),(LivingEntity)e)),
					entity->policy().candidate().accepts(body.contact(entity.getBoundingBox(),from,direction,distance),candidateWall,fluid,hit.contains(entity.getUUID())),
					entity->body.contact(entity.getBoundingBox(),from,direction,distance));
			boolean piercing=projectile.spell().piercing();
			return com.example.examplemod.magic.mechanic.projectile.ContactTraversal.apply(targets,piercing,
					new com.example.examplemod.magic.mechanic.projectile.ContactTraversal.World<net.minecraft.world.entity.Entity>() {
						public double contact(net.minecraft.world.entity.Entity target) {return body.contact(target.getBoundingBox(),from,direction,distance);}
						public boolean skip(net.minecraft.world.entity.Entity target,double contact) {
							return policy().skip(contact,distance,hit.contains(target.getUUID()));
		}
		public void claim(net.minecraft.world.entity.Entity target) {if(!policy().burstContacts())hit.add(target.getUUID());}
		public void position(double contact) {projectile.setPos(from.add(direction.scale(contact)));}
		public void effect(net.minecraft.world.entity.Entity target) {if(policy().burstContacts())ExodusFlameBurstAdapter.apply(projectile,level,hit);else ProjectileHitComposition.hurt(projectile,(LivingEntity)target,projectile.spell(),projectile.damage());}
		public void particles() {SweptProjectileImpactAdapter.impactParticles(projectile);}
		public void discard() {projectile.discard();}
	});
		}
		public boolean terminate() {
			final BlockPos terminalBlock=block;
			final Vec3 terminalImpact=impact;
			return com.example.examplemod.magic.mechanic.projectile.ProjectileTermination.apply(fluid,wall,distance,block!=null,
					new com.example.examplemod.magic.mechanic.projectile.ProjectileTermination.World() {
						public void position(double contact) {projectile.setPos(from.add(direction.scale(contact)));}
						public void wallEffect() {
							if(policy().burstContacts())ExodusFlameBurstAdapter.apply(projectile,level,hit);
							if(projectile.getOwner() instanceof ServerPlayer owner&&policy().blockBurstWidth()>0)
								SweptProjectileImpactAdapter.blockImpact(projectile,owner,terminalBlock,terminalImpact);
		}
		public void particles() {SweptProjectileImpactAdapter.impactParticles(projectile);}
		public void discard() {projectile.discard();}
	});
		}
		public void move() {projectile.setPos(to);travel.accept(distance);}

}
