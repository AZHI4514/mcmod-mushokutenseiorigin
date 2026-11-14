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

/** World boundary for TerrainPermission operations. */
public final class TerrainPermissionAdapter {
	public static boolean mayChange(ServerPlayer owner,BlockPos pos) {
		var level=(ServerLevel)owner.level();
		return level.isInWorldBounds(pos)&&level.getWorldBorder().isWithinBounds(pos)&&level.hasChunkAt(pos)&&owner.mayBuild()&&owner.mayInteract(level,pos)
				&&!owner.blockActionRestricted(level,pos,owner.gameMode.getGameModeForPlayer())&&!level.getServer().isUnderSpawnProtection(level,pos,owner);
	}
	private TerrainPermissionAdapter(){}
}
