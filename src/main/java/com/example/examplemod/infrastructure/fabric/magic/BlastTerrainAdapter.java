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

/** World boundary for BlastTerrain operations. */
public final class BlastTerrainAdapter {
	public static ItemStack woodenTool(BlockState state) {
		for(var item:List.of(Items.WOODEN_PICKAXE,Items.WOODEN_AXE,Items.WOODEN_SHOVEL,Items.WOODEN_HOE,Items.WOODEN_SWORD)) {
			var tool=new ItemStack(item);
			if(!state.requiresCorrectToolForDrops()||tool.isCorrectToolForDrops(state)) return tool;
		}
		return ItemStack.EMPTY;
	}
	public static void blast(ServerPlayer owner,BlockPos anchor,Vec3 hit,int width,boolean ignite) {
		var level=(ServerLevel)owner.level();
		for(var pos:com.example.examplemod.infrastructure.fabric.geometry.BlockBurstGeometryAdapter.blastBlocks(anchor,width,hit)) {
			if(!TerrainPermissionAdapter.mayChange(owner,pos)) continue;
			var state=level.getBlockState(pos); var tool=woodenTool(state);
			if(state.isAir()||state.is(TEMPORARY_ICE)||state.getDestroySpeed(level,pos)<0||!state.getFluidState().isEmpty()||tool.isEmpty()) continue;
			java.util.List<ItemStack> drops=java.util.List.of();
			for(var item:List.of(Items.WOODEN_PICKAXE,Items.WOODEN_AXE,Items.WOODEN_SHOVEL,Items.WOODEN_HOE,Items.WOODEN_SWORD)) {
				var candidate=new ItemStack(item);
				if(state.requiresCorrectToolForDrops()&&!candidate.isCorrectToolForDrops(state)) continue;
				var loot=Block.getDrops(state,level,pos,level.getBlockEntity(pos),owner,candidate);
				if(!loot.isEmpty()) { drops=loot; break; }
			}
			if(drops.isEmpty()) continue;
			drops.forEach(stack->Block.popResource(level,pos,stack));
			level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
		}
		if(ignite) for(var pos:com.example.examplemod.infrastructure.fabric.geometry.BlockBurstGeometryAdapter.blastBlocks(anchor,width,hit)) {
			if(!TerrainPermissionAdapter.mayChange(owner,pos)||!level.getBlockState(pos).isAir()) continue;
			var fire=BaseFireBlock.getState(level,pos);
			if(fire.canSurvive(level,pos)) level.setBlockAndUpdate(pos,fire);
		}
	}
	private BlastTerrainAdapter(){}
}
