package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import static com.example.examplemod.magic.MagicTerrain.*;

import com.example.examplemod.ExampleMod;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import java.util.List;

/** World boundary for TemporaryTerrain operations. */
public final class TemporaryTerrainAdapter {
	public static boolean temporary(ServerPlayer owner,BlockPos pos,Block material,int ticks) {
		var level=(ServerLevel)owner.level();
		if(!TerrainPermissionAdapter.mayChange(owner,pos)||!level.getBlockState(pos).isAir()||!level.setBlockAndUpdate(pos,material.defaultBlockState())) return false;
		if(level.getBlockEntity(pos) instanceof IceExpiry timer) { timer.expireAt(level.getGameTime()+ticks); }
		level.scheduleTick(pos,material,ticks); return true;
	}
	public static void encase(ServerPlayer owner,net.minecraft.world.entity.LivingEntity target) {
		var level=(ServerLevel)owner.level(); var middle=BlockPos.containing(target.getBoundingBox().getCenter());
		for(var pos:BlockPos.betweenClosed(middle.offset(-1,-1,-1),middle.offset(1,1,1))) {
			if(TerrainPermissionAdapter.mayChange(owner,pos)&&level.getBlockState(pos).isAir()&&level.setBlockAndUpdate(pos,TEMPORARY_ICE.defaultBlockState())) {
				if(level.getBlockEntity(pos) instanceof IceExpiry timer) { timer.expireAt(level.getGameTime()+120); }
				level.scheduleTick(pos,TEMPORARY_ICE,120);
			}
		}
	}
	private TemporaryTerrainAdapter(){}
}
