package com.example.examplemod.infrastructure.fabric.magic;
import static com.example.examplemod.infrastructure.fabric.magic.EffectCarrierSpawner.entity;
import static com.example.examplemod.infrastructure.fabric.magic.CarrierDamageAdapter.damage;

import java.util.*;
import com.example.examplemod.magic.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
import static com.example.examplemod.infrastructure.fabric.magic.SpellRaycastAdapter.*;
import static com.example.examplemod.infrastructure.fabric.magic.EntityImpulseAdapter.launch;

/** The legacy self-lift construction path, now owned only by earth lance. */
public final class EarthLanceConstructionAdapter {
	private static List<BlockPos> plan(ServerPlayer player,MagicAim direction){
		var hit=SpellRaycastAdapter.aim(player,direction);var result=new ArrayList<BlockPos>();
		if(hit==null||hit.getType()!=HitResult.Type.BLOCK||hit.getDirection()!=net.minecraft.core.Direction.UP)return result;
		var start=hit.getBlockPos().above();for(int y=0;y<3;y++)result.add(start.offset(0,y,0));return result;
	}
	public static boolean construct(ServerPlayer player,MagicAim direction) {
		var spell=SpellDefinition.EARTH_LANCE;var plan=plan(player,direction); if(plan.isEmpty()||!com.example.examplemod.infrastructure.fabric.magic.ColumnPlacementAdapter.canPlace(player,plan)) return false;
		var level=(ServerLevel)player.level(); var bounds=new AABB(plan.getFirst()).minmax(new AABB(plan.getLast()));
		boolean self=plan.getFirst().below().equals(player.blockPosition().below())&&player.getBoundingBox().intersects(bounds);
		var targets=new ArrayList<net.minecraft.world.entity.Entity>(level.getEntities(player,bounds,e->e instanceof LivingEntity));
		if(player.getBoundingBox().intersects(bounds)) targets.add(player);
		if(self) { com.example.examplemod.infrastructure.fabric.magic.EarthLanceLiftAdapter.apply(player,plan,targets,direction);return true; }
		for(var target:targets) {
			launch(target,com.example.examplemod.infrastructure.fabric.geometry.LaunchImpulseAdapter.launchVelocity(new Vec3(0,1,0),3));
		}
		if(!self&&spell.damage()>0) {var carrier=entity(player,spell,Vec3.atCenterOf(plan.getFirst()),direction.direction());for(var target:targets) damage(carrier,(LivingEntity)target);}
		if(!targets.isEmpty()) { com.example.examplemod.infrastructure.fabric.magic.GrowingColumnRuntimeAdapter.growingColumn(player,plan,spell,self); SpellVisuals.burst(level,spell,Vec3.atBottomCenterOf(plan.getFirst()),direction.direction()); return true; }
		for(var pos:plan) {
			if(self) level.setBlockAndUpdate(pos,Blocks.DIRT.defaultBlockState());
			else com.example.examplemod.infrastructure.fabric.magic.TemporaryTerrainAdapter.temporary(player,pos,MagicTerrain.TEMPORARY_DIRT,200);
		}
		SpellVisuals.burst(level,spell,Vec3.atBottomCenterOf(plan.getFirst()),direction.direction());
		if(!self&&spell.damage()>0) {
			var carrier=entity(player,spell,Vec3.atCenterOf(plan.getFirst()),direction.direction());
			for(var target:targets) damage(carrier,(LivingEntity)target);
		}
		return true;
	}
	private EarthLanceConstructionAdapter(){}
}
