package com.example.examplemod.magic;

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

/** Registry and vanilla temporary-block lifecycle; terrain permissions and edits live in dedicated adapters. */
public final class MagicTerrain {
	public static final Block EXPANSION_ICE=Registry.register(BuiltInRegistries.BLOCK,ExampleMod.id("expansion_ice"),new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ICE).noLootTable().pushReaction(net.minecraft.world.level.material.PushReaction.BLOCK).setId(ResourceKey.create(Registries.BLOCK,ExampleMod.id("expansion_ice")))));
	public static final Block SCORCHED_SURFACE=Registry.register(BuiltInRegistries.BLOCK,ExampleMod.id("scorched_surface"),new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGMA_BLOCK).noLootTable().pushReaction(net.minecraft.world.level.material.PushReaction.BLOCK).setId(ResourceKey.create(Registries.BLOCK,ExampleMod.id("scorched_surface")))));
	public static final Block EXPANSION_DIRT=Registry.register(BuiltInRegistries.BLOCK,ExampleMod.id("expansion_dirt"),new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).noLootTable().pushReaction(net.minecraft.world.level.material.PushReaction.BLOCK).setId(ResourceKey.create(Registries.BLOCK,ExampleMod.id("expansion_dirt")))));
	public static final Block TEMPORARY_ICE=Registry.register(BuiltInRegistries.BLOCK,ExampleMod.id("temporary_magic_ice"),new TemporaryIce(
			BlockBehaviour.Properties.ofFullCopy(Blocks.ICE).noLootTable().pushReaction(net.minecraft.world.level.material.PushReaction.BLOCK)
					.setId(ResourceKey.create(Registries.BLOCK,ExampleMod.id("temporary_magic_ice")))));
	public static final Block TEMPORARY_DIRT=Registry.register(BuiltInRegistries.BLOCK,ExampleMod.id("temporary_magic_dirt"),new TemporaryIce(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).noLootTable().pushReaction(net.minecraft.world.level.material.PushReaction.BLOCK).setId(ResourceKey.create(Registries.BLOCK,ExampleMod.id("temporary_magic_dirt")))));

	public static final net.minecraft.world.level.block.entity.BlockEntityType<IceExpiry> ICE_EXPIRY=Registry.register(
			BuiltInRegistries.BLOCK_ENTITY_TYPE,ExampleMod.id("magic_ice_expiry"),
			net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create(IceExpiry::new,TEMPORARY_ICE,TEMPORARY_DIRT).build());
	public static final class IceExpiry extends net.minecraft.world.level.block.entity.BlockEntity {
		private long expiry;
		public void expireAt(long value){expiry=value;setChanged();}
		public IceExpiry(BlockPos pos,BlockState state) { super(ICE_EXPIRY,pos,state); }
		@Override protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput output) { super.saveAdditional(output); output.putLong("ExpiresAt",expiry); }
		@Override protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) { super.loadAdditional(input); expiry=input.getLongOr("ExpiresAt",0); }
	}
	public static final class TemporaryIce extends Block implements net.minecraft.world.level.block.EntityBlock {
		public static final MapCodec<TemporaryIce> CODEC=simpleCodec(TemporaryIce::new);
		public TemporaryIce(Properties properties) { super(properties); }
		@Override public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos,BlockState state) { return new IceExpiry(pos,state); }
		@Override protected MapCodec<? extends Block> codec() { return CODEC; }
		@Override protected void onPlace(BlockState state,Level level,BlockPos pos,BlockState old,boolean moved) {
			if(!old.is(this)&&level instanceof ServerLevel server) {
				if(server.getBlockEntity(pos) instanceof IceExpiry timer) { timer.expiry=server.getGameTime()+120; timer.setChanged(); }
				server.scheduleTick(pos,this,120);
			}
		}
		@Override protected void tick(BlockState state,ServerLevel level,BlockPos pos,RandomSource random) {
			if(level.getBlockEntity(pos) instanceof IceExpiry timer && timer.expiry>level.getGameTime()) {
				level.scheduleTick(pos,this,(int)Math.min(120,timer.expiry-level.getGameTime())); return;
			}
			if(level.getBlockState(pos).is(this)) { level.levelEvent(2001,pos,Block.getId(state)); level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState()); }
		}
	}


	/** 原版木工具采集条件；例如石头可采但铁矿不可用木镐采集。 */


	public static void initialize() { }
	private MagicTerrain() { }
}
