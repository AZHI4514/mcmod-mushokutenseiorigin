package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.projectile.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;
import net.minecraft.core.BlockPos;
import java.util.function.*;

final class IceLanceCarrierEffect implements StationaryCarrierEffect {
	public void apply(DirectionalSpellCarrier carrier,ServerPlayer owner,ServerLevel level,float yaw,int age){
			com.example.examplemod.magic.spell.water.ice_lance.IceLanceHit.apply(age,
					com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(carrier.center()),
					com.example.examplemod.infrastructure.fabric.geometry.GeometryAdapter.vector(Vec3.directionFromRotation(0,yaw)),
					new com.example.examplemod.infrastructure.fabric.magic.IceLanceHitAdapter(carrier,level));
	}
}
