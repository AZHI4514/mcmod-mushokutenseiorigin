package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.earth.earth_fortress.EarthFortressLayout;
import com.example.examplemod.magic.application.port.SelfEffect;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;

/** Hollow voxel dome, with a two-block doorway in the release direction. */
public record EarthFortressAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return cast(player,new MagicAim(yaw,pitch));}
	public static List<BlockPos> shell(BlockPos base,Vec3 look){
		return EarthFortressLayout.shell(look.x,look.z).stream().map(cell->base.offset(cell.x(),cell.y(),cell.z())).toList();
	}
	public static boolean cast(ServerPlayer owner,MagicAim aim){
		var plan=new LinkedHashMap<BlockPos,BlockState>();for(var pos:shell(owner.blockPosition(),aim.direction())){if(plan.size()>=200)break;plan.put(pos,MagicTerrain.EXPANSION_DIRT.defaultBlockState());}
		if(!TerrainLeaseRepository.enqueue(owner,plan,2400,false))return false;
		SpellVisuals.burst((ServerLevel)owner.level(),SpellDefinition.EARTH_FORTRESS,owner.position().add(0,1,0),aim.direction());return true;
	}
}
