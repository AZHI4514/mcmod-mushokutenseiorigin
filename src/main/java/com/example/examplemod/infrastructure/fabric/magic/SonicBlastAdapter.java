package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.wind.sonic_blast.*;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;

/** Synchronous pressure wave; the configured source is never spawned. */
public final class SonicBlastAdapter implements SelfEffect {
	private final ServerPlayer owner;
	public SonicBlastAdapter(ServerPlayer owner) {this.owner=owner;}
	public boolean apply(float yaw,float pitch) {return cast(owner,new MagicAim(yaw,pitch));}
	public static boolean cast(ServerPlayer owner,MagicAim aim) {
		var level=(ServerLevel)owner.level();
		var from=owner.getEyePosition();
		var direction=aim.direction();
		var source=new MagicAreaEntity(MagicSystem.MAGIC_AREA,level);
		source.configure(owner,SpellDefinition.SONIC_BLAST,aim);
		source.setPos(from);
		for(var entity:level.getEntities(owner,new AABB(from,from).inflate(SonicBlastRules.RANGE),e->e instanceof LivingEntity&&MagicEffects.allowed(owner,(LivingEntity)e))) {
			var target=(LivingEntity)entity;
			var offset=target.getBoundingBox().getCenter().subtract(from);
			if(!contains(offset,direction)||!SpellVisibilityAdapter.visible(level,from,target,source)) continue;
			SonicBlastHit.apply(new SonicBlastHit.Target() {
				public boolean hurt() {return target.hurtServer(level,MagicDamage.source(source,SpellDefinition.SONIC_BLAST),SpellDefinition.SONIC_BLAST.damage());}
				public boolean impulseImmune() {return MagicControl.immune(target);}
				public void impulse(double strength,boolean useAim) {
					target.setDeltaMovement(target.getDeltaMovement().add(useAim?direction.scale(strength):offset.normalize().scale(strength)));
					target.hurtMarked=true;
				}
				public void afterHit() {SpellHooks.afterHit(source,target);}
			},offset.lengthSqr());
		}
		if(MagicConfig.permanentTerrain()) {
			var candidates=new ArrayList<BlockPos>();
			for(var pos:BlockPos.betweenClosed(BlockPos.containing(from).offset(-16,-16,-16),BlockPos.containing(from).offset(16,16,16))) {
				if(contains(Vec3.atCenterOf(pos).subtract(from),direction)&&level.hasChunkAt(pos)) candidates.add(pos.immutable());
			}
			candidates.sort(Comparator.comparingDouble(pos->Vec3.atCenterOf(pos).distanceToSqr(from)));
			SonicBlastTerrain.apply(candidates,new SonicBlastTerrain.World<BlockPos>() {
				public boolean visible(BlockPos pos) {
					var hit=level.clip(new ClipContext(from,Vec3.atCenterOf(pos),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,owner));
					return hit.getType()==HitResult.Type.BLOCK&&hit.getBlockPos().equals(pos);
				}
				public boolean breakBlock(BlockPos pos) {return PermanentTerrainAdapter.breakStoneHarvestable(owner,pos);}
			});
		}
		SpellVisuals.burst(level,SpellDefinition.SONIC_BLAST,from,direction);
		return true;
	}
	private static boolean contains(Vec3 offset,Vec3 direction) {
		return SonicBlastRules.contains(offset.x,offset.y,offset.z,direction.x,direction.y,direction.z);
	}
}
