package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.fire.heat_hand.*;

import net.minecraft.core.BlockPos;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/** 融化准星命中面上的2×2方块；服务端检查模式、出生点保护及修改权限。 */
public final class HeatHandAdapter implements SelfEffect {
	private final ServerPlayer player;
	public HeatHandAdapter(ServerPlayer player){this.player=player;}
	public boolean apply(float yaw,float pitch){return execute(player,new MagicAim(yaw,pitch));}
	private static List<BlockPos> meltTargets(ServerPlayer player, MagicAim aim) {
		var level=player.level(); var from=player.getEyePosition();
		var hit=level.clip(new ClipContext(from,from.add(aim.direction().scale(4)),ClipContext.Block.OUTLINE,ClipContext.Fluid.NONE,player));
		if(hit.getType()!=HitResult.Type.BLOCK) return List.of();
		var blockHit=(BlockHitResult)hit; var face=blockHit.getDirection();
		var anchor=blockHit.getBlockPos();var result=new ArrayList<BlockPos>();
		for(var cell:HeatHandRules.targets(anchor.getX(),anchor.getY(),anchor.getZ(),HeatHandRules.Axis.valueOf(face.getAxis().name()))){
			var pos=new BlockPos(cell.x(),cell.y(),cell.z());if(meltable(player,pos))result.add(pos);
		}
		return result;
	}
	private static boolean meltable(ServerPlayer player,BlockPos pos) {
		var level=player.level();
		if(!level.hasChunkAt(pos)||!player.mayBuild()||!player.mayInteract((net.minecraft.server.level.ServerLevel)level,pos)
				||player.blockActionRestricted(level,pos,player.gameMode.getGameModeForPlayer())
				||level.getServer().isUnderSpawnProtection((net.minecraft.server.level.ServerLevel)level,pos,player)) return false;
		var state=level.getBlockState(pos);
		return state.is(Blocks.SNOW)||state.is(Blocks.SNOW_BLOCK)||state.is(Blocks.ICE)||state.is(Blocks.PACKED_ICE)
				||state.is(Blocks.BLUE_ICE)||state.is(Blocks.FROSTED_ICE)||state.is(Blocks.POWDER_SNOW);
	}
	public static boolean canUse(ServerPlayer player, MagicAim aim) {
		var warmth = player.getEffect(MagicEffects.WARMTH);
		return !meltTargets(player, aim).isEmpty() || player.getTicksFrozen() > 0 || warmth == null;
	}
	public static boolean execute(ServerPlayer player,MagicAim aim){
		return HeatHandMelt.apply(new HeatHandMelt.World<BlockPos>(){
			public Iterable<BlockPos> targets(){return meltTargets(player,aim);}
			public boolean melt(BlockPos pos){
				var level=player.level();var old=level.getBlockState(pos);
				boolean ice=old.is(Blocks.ICE)||old.is(Blocks.PACKED_ICE)||old.is(Blocks.BLUE_ICE)||old.is(Blocks.FROSTED_ICE);
				boolean evaporates=level.environmentAttributes().getValue(net.minecraft.world.attribute.EnvironmentAttributes.WATER_EVAPORATES,pos);
				return level.setBlockAndUpdate(pos,HeatHandRules.becomesWater(ice,evaporates)?Blocks.WATER.defaultBlockState():Blocks.AIR.defaultBlockState());
			}
			public boolean hasWarmth(){return player.getEffect(MagicEffects.WARMTH)!=null;}
			public int frozenTicks(){return player.getTicksFrozen();}
			public void warm(){MagicEffects.warm(player,200);}
			public void particles(BlockPos pos){((net.minecraft.server.level.ServerLevel)player.level()).sendParticles(SpellParticles.burst(SpellDefinition.HEAT_HAND),pos.getX()+0.5,pos.getY()+1,pos.getZ()+0.5,3,0.2,0.1,0.2,0.015);}
		});
	}
}
