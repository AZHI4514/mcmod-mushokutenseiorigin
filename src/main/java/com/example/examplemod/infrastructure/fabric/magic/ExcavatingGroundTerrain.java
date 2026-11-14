package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import net.minecraft.server.level.*;
import net.minecraft.core.*;
public record ExcavatingGroundTerrain(int depth,int limit) implements GroundTerrainEffect {
	public void apply(ServerPlayer owner,MagicAreaEntity area,int pulse) {
		if(pulse==0)TerrainLeaseRepository.excavate(owner,area.position(),(int)GroundFieldRuntimeAdapter.radius(area.spell()),depth,limit);
	}
}
