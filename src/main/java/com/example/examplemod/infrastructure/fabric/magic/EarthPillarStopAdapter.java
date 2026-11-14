package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.earth.earth_pillar.EarthPillarStop;
import net.minecraft.server.level.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
public final class EarthPillarStopAdapter implements EarthPillarStop.World {
	private final DirectionalSpellCarrier entity;private final ServerPlayer owner;private final ServerLevel level;
	public EarthPillarStopAdapter(DirectionalSpellCarrier entity,ServerPlayer owner,ServerLevel level){this.entity=entity;this.owner=owner;this.level=level;}
	public void move(double distance){entity.setPos(entity.position().add(0,-distance,0));entity.updateBounds();}
	public void show(){SpellVisuals.burst(level,entity.spell(),entity.center(),new Vec3(0,-1,0));}
	public boolean hasOwner(){return owner!=null;}
	public void stone(){EarthPillarAdapter.permanentStone(owner,BlockPos.containing(entity.getX(),Math.ceil(entity.getY()-1.0E-6),entity.getZ()));}
	public void discard(){entity.discard();}
}
