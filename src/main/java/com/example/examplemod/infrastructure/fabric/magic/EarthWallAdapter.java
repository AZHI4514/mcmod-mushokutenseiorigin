package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.earth.earth_wall.EarthWallLayout;
import com.example.examplemod.magic.application.port.SelfEffect;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;

/** All-or-nothing, temporary five-by-three earth wall. */
public record EarthWallAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return cast(player,new MagicAim(yaw,pitch));}
	public static Map<BlockPos,BlockState> plan(ServerPlayer owner,MagicAim aim){
	 var level=(ServerLevel)owner.level();var from=owner.getEyePosition();Vec3 at=from;
	 for(int i=0;i<24;i++){var a=from.add(aim.direction().scale(i));var b=from.add(aim.direction().scale(i+1));if(!SpellRaycastAdapter.loaded(level,new AABB(a,b)))return Map.of();var hit=level.clip(new ClipContext(a,b,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,owner));at=hit.getLocation();if(hit.getType()==HitResult.Type.BLOCK){at=at.add(hit.getDirection().getStepX()*.02,hit.getDirection().getStepY()*.02,hit.getDirection().getStepZ()*.02);break;}}
	 var base=BlockPos.containing(at);var result=new LinkedHashMap<BlockPos,BlockState>();
	 for(var cell:EarthWallLayout.offsets(aim.direction().x,aim.direction().z))result.put(base.offset(cell.x(),cell.y(),cell.z()),MagicTerrain.EXPANSION_DIRT.defaultBlockState());return result;
	}
	public static boolean cast(ServerPlayer owner,MagicAim aim){var plan=plan(owner,aim);if(plan.isEmpty()||!TerrainLeaseRepository.enqueue(owner,plan,1200,true))return false;SpellVisuals.burst((ServerLevel)owner.level(),SpellDefinition.EARTH_WALL,Vec3.atCenterOf(plan.keySet().iterator().next()).add(0,1,0),aim.direction());return true;}
}
