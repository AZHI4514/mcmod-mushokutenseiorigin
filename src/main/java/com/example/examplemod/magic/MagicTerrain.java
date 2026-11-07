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

/** 独立临时冰以方块实体记录到期时间，计划tick清理；没有物品或掉落。 */
public final class MagicTerrain {
	public static final Block TEMPORARY_ICE=Registry.register(BuiltInRegistries.BLOCK,ExampleMod.id("temporary_magic_ice"),new TemporaryIce(
			BlockBehaviour.Properties.ofFullCopy(Blocks.ICE).noLootTable().pushReaction(net.minecraft.world.level.material.PushReaction.BLOCK)
					.setId(ResourceKey.create(Registries.BLOCK,ExampleMod.id("temporary_magic_ice")))));
	public static final net.minecraft.world.level.block.entity.BlockEntityType<IceExpiry> ICE_EXPIRY=Registry.register(
			BuiltInRegistries.BLOCK_ENTITY_TYPE,ExampleMod.id("magic_ice_expiry"),
			net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.create(IceExpiry::new,TEMPORARY_ICE).build());
	public static final class IceExpiry extends net.minecraft.world.level.block.entity.BlockEntity {
		long expiry;
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
	public static boolean mayChange(ServerPlayer owner,BlockPos pos) {
		var level=(ServerLevel)owner.level();
		return level.hasChunkAt(pos)&&owner.mayBuild()&&owner.mayInteract(level,pos)
				&&!owner.blockActionRestricted(level,pos,owner.gameMode.getGameModeForPlayer())&&!level.getServer().isUnderSpawnProtection(level,pos,owner);
	}
	public static void encase(ServerPlayer owner,net.minecraft.world.entity.LivingEntity target) {
		var level=(ServerLevel)owner.level(); var middle=BlockPos.containing(target.getBoundingBox().getCenter());
		for(var pos:BlockPos.betweenClosed(middle.offset(-1,-1,-1),middle.offset(1,1,1))) {
			if(mayChange(owner,pos)&&level.getBlockState(pos).isAir()&&level.setBlockAndUpdate(pos,TEMPORARY_ICE.defaultBlockState())) {
				if(level.getBlockEntity(pos) instanceof IceExpiry timer) { timer.expiry=level.getGameTime()+120; timer.setChanged(); }
				level.scheduleTick(pos,TEMPORARY_ICE,120);
			}
		}
	}
	/** 原版木工具采集条件；例如石头可采但铁矿不可用木镐采集。 */
	public static ItemStack woodenTool(BlockState state) {
		for(var item:List.of(Items.WOODEN_PICKAXE,Items.WOODEN_AXE,Items.WOODEN_SHOVEL,Items.WOODEN_HOE,Items.WOODEN_SWORD)) {
			var tool=new ItemStack(item);
			if(!state.requiresCorrectToolForDrops()||tool.isCorrectToolForDrops(state)) return tool;
		}
		return ItemStack.EMPTY;
	}
	public static void blast(ServerPlayer owner,BlockPos anchor,Vec3 hit,int width,boolean ignite) {
		var level=(ServerLevel)owner.level();
		for(var pos:IntermediateRules.blastBlocks(anchor,width,hit)) {
			if(!mayChange(owner,pos)) continue;
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
		if(ignite) for(var pos:IntermediateRules.blastBlocks(anchor,width,hit)) {
			if(!mayChange(owner,pos)||!level.getBlockState(pos).isAir()) continue;
			var fire=BaseFireBlock.getState(level,pos);
			if(fire.canSurvive(level,pos)) level.setBlockAndUpdate(pos,fire);
		}
	}
	public static void initialize() { }
	private MagicTerrain() { }
}
