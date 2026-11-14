package com.example.examplemod.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;

/** Explicit opt-in permanent terrain; never changes the original spell policy. */
public final class PermanentTerrainAdapter {
	public static boolean breakStoneHarvestable(ServerPlayer owner,BlockPos pos){
		if(!MagicConfig.permanentTerrain()||!com.example.examplemod.infrastructure.fabric.magic.TerrainPermissionAdapter.mayChange(owner,pos))return false;
		var level=owner.level();var state=level.getBlockState(pos);
		if(state.isAir()||state.hasBlockEntity()||state.getDestroySpeed(level,pos)<0||!state.getFluidState().isEmpty()||state.is(MagicTerrain.TEMPORARY_ICE)||state.is(MagicTerrain.TEMPORARY_DIRT)||state.is(MagicTerrain.EXPANSION_ICE)||state.is(MagicTerrain.EXPANSION_DIRT)||state.is(MagicTerrain.SCORCHED_SURFACE))return false;
		boolean harvestable=!state.requiresCorrectToolForDrops();
		for(var item:new Item[]{Items.STONE_PICKAXE,Items.STONE_AXE,Items.STONE_SHOVEL,Items.STONE_HOE})if(new ItemStack(item).isCorrectToolForDrops(state))harvestable=true;
		return harvestable&&level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
	}
	private PermanentTerrainAdapter(){}
}
