package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.projectile.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;
import net.minecraft.core.BlockPos;
import java.util.function.*;

public final class CarrierFlightAdapter {
	private final DirectionalSpellCarrier carrier;
	private final ServerPlayer owner;
	private final ServerLevel level;
	private final ImpactHistory hit;
	private final DoubleSupplier speed;
	private final float yaw;
	private CarrierFlightPolicy policy(){return CarrierFlightPolicy.of(spell());}
	private SpellDefinition spell(){return carrier.spell();}
	private Vec3 center(){return carrier.center();}
	private Vec3 position(){return carrier.position();}
	private Vec3 direction(){return carrier.direction();}
	private SpellSweep volume(){return carrier.volume();}
	private AABB getBoundingBox(){return carrier.getBoundingBox();}
	private Entity getOwner(){return carrier.getOwner();}
	private java.util.UUID getUUID(){return carrier.getUUID();}
	private void setPos(Vec3 pos){carrier.setPos(pos);}
	private void updateBounds(){carrier.updateBounds();}
	private void discard(){carrier.discard();}
	private double speed(){return speed.getAsDouble();}
	private final DoubleConsumer travel;
	private void explode(){explode(false);}
	private void explode(boolean keepFlying){com.example.examplemod.magic.spell.fire.explosion.ExplosionImpact.apply(keepFlying,new ExplosionImpactAdapter(carrier,level));}
	public CarrierFlightAdapter(DirectionalSpellCarrier carrier,ServerPlayer owner,ServerLevel level,ImpactHistory hit,DoubleSupplier speed,float yaw,DoubleConsumer travel) {
		this.carrier=carrier;this.owner=owner;this.level=level;this.hit=hit;this.speed=speed;this.yaw=yaw;this.travel=travel;
	}
	public void apply() {
		var from=center(); var motion=direction(); double distance=speed();
		if(distance<=0) { discard(); return; }
		var volume=volume(); var to=from.add(motion.scale(distance)); var bounds=volume.bounds(from).minmax(volume.bounds(to));
		if(!SpellRaycastAdapter.loaded(level,bounds))return;
		double wall=Double.POSITIVE_INFINITY; BlockPos block=null; var shapes=new java.util.ArrayList<AABB>();
		for(var pos:BlockPos.betweenClosed(BlockPos.containing(bounds.minX,bounds.minY,bounds.minZ),BlockPos.containing(bounds.maxX,bounds.maxY,bounds.maxZ))) {
			if(MagicFlight.passesWaterBlock(carrier,pos)) continue;
			var state=level.getBlockState(pos);var collisions=state.getCollisionShape(level,pos).toAabbs();
			for(var shape:collisions) {
				shapes.add(shape.move(pos)); double contact=volume.contact(shape.move(pos),from,motion,distance);
				if(contact<wall) { wall=contact; block=pos.immutable(); }
			}
		}
		if(policy().bladeSection()) wall=new com.example.examplemod.magic.domain.geometry.BladeSectionVolume(com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(motion),-yaw).blockingDistance(com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(from),distance,shapes.stream().map(com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter::box).toList());
		double fluid=MagicFlight.fluidHit(carrier,volume,from,motion.scale(distance));
		final double terminalWall=wall;
		var candidates=ContactCandidates.collect(level.getEntities(carrier,bounds,e->e instanceof LivingEntity&&MagicEffects.allowed(owner,(LivingEntity)e)),entity->policy().candidate(volume.contact(entity.getBoundingBox(),from,motion,distance),terminalWall,fluid,hit.contains(entity.getUUID())),
				t->volume.contact(t.getBoundingBox(),from,motion,distance));
		if(CarrierContactTraversal.apply(candidates,new CarrierContactTraversal.World<Entity>() {
			public double contact(Entity target){return volume.contact(target.getBoundingBox(),from,motion,distance);}
			public boolean skip(Entity target,double contact){return policy().skip(contact,distance,hit.contains(target.getUUID()));}
			public void claim(Entity target){hit.claim(target.getUUID());}
			public boolean explosion(){return policy().explosion();}
			public void position(double contact){setPos(from.add(motion.scale(contact)));}
			public boolean piercing(){return spell().piercing();}
			public void explode(boolean keepFlying){CarrierFlightAdapter.this.explode(keepFlying);}
			public void effect(Entity target){
				if(policy().windSlice()) com.example.examplemod.magic.spell.wind.wind_slice.WindSliceHit.apply(hit,new WindSliceHitAdapter(carrier,level,(LivingEntity)target));
				else CarrierDamageAdapter.damage(carrier,(LivingEntity)target);
			}
			public void burst(double contact){SpellVisuals.burst(level,spell(),from.add(motion.scale(contact)),motion);}
			public void discard(){carrier.discard();}
		}))return;

		if(CarrierTermination.apply(policy().explosion(),distance,wall,fluid,block!=null,new CarrierTermination.World(){
			public void discard(){carrier.discard();}
			public void explosionPosition(double distance){setPos(from.add(motion.scale(distance)));}
			public void explode(){CarrierFlightAdapter.this.explode();}
			public void wallBurst(double distance){SpellVisuals.burst(level,spell(),from.add(motion.scale(distance)),motion);}
		}))return;

		var next=to;
		setPos(next); travel.accept(distance); updateBounds();
	}
}
