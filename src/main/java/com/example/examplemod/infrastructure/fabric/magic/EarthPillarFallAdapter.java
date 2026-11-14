package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.projectile.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;
import net.minecraft.core.BlockPos;
import java.util.function.*;

public final class EarthPillarFallAdapter {
	private final DirectionalSpellCarrier carrier;
	private final ServerPlayer owner;
	private final ServerLevel level;
	private final ImpactHistory hit;
	private final DoubleSupplier speed;
	private final float yaw;
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
	public EarthPillarFallAdapter(DirectionalSpellCarrier carrier,ServerPlayer owner,ServerLevel level,ImpactHistory hit,DoubleSupplier speed,float yaw) {
		this.carrier=carrier;this.owner=owner;this.level=level;this.hit=hit;this.speed=speed;this.yaw=yaw;
	}
	public void apply() {
		var from=center();var motion=new Vec3(0,-1,0);double distance=speed();
		var body=new SpellSweep(new Vec3(0,0,1),3,3,3);
		var bounds=body.bounds(from).minmax(body.bounds(from.add(0,-distance,0)));
		if(!SpellRaycastAdapter.loaded(level,bounds))return;
		double stop=MagicFlight.fluidHit(carrier,body,from,motion.scale(distance));
		for(var pos:BlockPos.betweenClosed(BlockPos.containing(bounds.minX,bounds.minY,bounds.minZ),BlockPos.containing(bounds.maxX,bounds.maxY,bounds.maxZ))) {
			var state=level.getBlockState(pos);
			if(!state.isAir()&&state.getFluidState().isEmpty())stop=Math.min(stop,body.contact(new AABB(pos),from,motion,distance));
		}
		for(var target:level.getEntities(carrier,bounds,e->e instanceof LivingEntity&&MagicEffects.allowed(getOwner(),(LivingEntity)e))) {
			double contact=body.contact(target.getBoundingBox(),from,motion,distance);
			if(com.example.examplemod.magic.spell.earth.earth_pillar.EarthPillarFall.contactDue(contact,stop,hit.contains(target.getUUID()))&&hit.claim(target.getUUID())) {
				CarrierDamageAdapter.damage(carrier,(LivingEntity)target);
				SpellVisuals.burst(level,spell(),from.add(motion.scale(contact)),motion);
			}
		}
		if(com.example.examplemod.magic.spell.earth.earth_pillar.EarthPillarFall.stopped(stop)) {
			com.example.examplemod.magic.spell.earth.earth_pillar.EarthPillarStop.apply(stop,new com.example.examplemod.infrastructure.fabric.magic.EarthPillarStopAdapter(carrier,owner,level));return;
		}
		setPos(position().add(0,-distance,0));updateBounds();
	}

}
