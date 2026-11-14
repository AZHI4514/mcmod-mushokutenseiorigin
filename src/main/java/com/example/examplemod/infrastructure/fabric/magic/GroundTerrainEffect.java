package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import net.minecraft.server.level.*;
import net.minecraft.core.*;
/** Terrain Strategy; actual lease permission/quota behavior remains in the terrain service. */
@FunctionalInterface
public interface GroundTerrainEffect {
	void apply(ServerPlayer owner,MagicAreaEntity area,int pulse);
}
