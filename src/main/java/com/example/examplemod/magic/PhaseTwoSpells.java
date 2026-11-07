package com.example.examplemod.magic;

import java.util.ArrayList;
import java.util.List;
import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;

/** Server execution and preflight for the eighteen phase-two spells. */
public final class PhaseTwoSpells {
	public static final TagKey<net.minecraft.world.entity.EntityType<?>> BIND_IMMUNE=TagKey.create(Registries.ENTITY_TYPE,ExampleMod.id("wind_bind_immune"));
	public static boolean isChannel(SpellDefinition spell) { return spell!=null && (spell.id().equals("waterfall")||spell.id().equals("flamethrower")); }
	public static BlockHitResult aim(ServerPlayer player,MagicAim aim) {
		var from=player.getEyePosition(); var end=from.add(aim.direction().scale(12));
		var level=(ServerLevel)player.level();
		if(!loaded(level,new AABB(from,end).inflate(0.01))) return null;
		return level.clip(new ClipContext(from,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,player));
	}
	public static boolean loaded(ServerLevel level,AABB box) {
		for(int x=(int)Math.floor(box.minX)>>4;x<=((int)Math.floor(box.maxX)>>4);x++)
			for(int z=(int)Math.floor(box.minZ)>>4;z<=((int)Math.floor(box.maxZ)>>4);z++)
				if(!level.hasChunkAt(new BlockPos(x<<4,0,z<<4))) return false;
		return true;
	}
	public static LivingEntity aimedEntity(ServerPlayer player,MagicAim direction) {
		var hit=aim(player,direction); if(hit==null) return null;
		var from=player.getEyePosition(); var to=hit.getLocation(); double nearest=from.distanceTo(to); LivingEntity selected=null;
		for(var candidate:player.level().getEntities(player,new AABB(from,to).inflate(1),e->e instanceof LivingEntity && MagicEffects.allowed(player,(LivingEntity)e))) {
			var intersection=candidate.getBoundingBox().inflate(0.1).clip(from,to);
			if(intersection.isPresent() && intersection.get().distanceTo(from)<nearest) { nearest=intersection.get().distanceTo(from); selected=(LivingEntity)candidate; }
		}
		return selected;
	}
	public static boolean canExecute(ServerPlayer player,SpellDefinition spell,MagicAim direction) {
		if(!direction.valid()) return false;
		return switch(spell.id()) {
			case "ex_healing" -> spell.healingAmount(player.getHealth(),player.getMaxHealth())>0;
			case "antidote_ii" -> player.hasEffect(MobEffects.POISON)||player.hasEffect(MobEffects.NAUSEA)||player.hasEffect(MobEffects.HUNGER);
			case "wind_bind","ice_pillar","ice_lance","explosion","waterfall","earth_lance" -> true;
			case "ice_fortress" -> { var plan=plan(player,spell,direction); yield !plan.isEmpty()&&preflight(player,plan); }
			case "flame_pillar" -> trace(player,direction,spell.range())!=null;
			case "earth_pillar" -> { var hit=aim(player,direction); yield hit!=null; }
			case "wind_blast" -> true;
			default -> true;
		};
	}
	public static PhaseTwoEntity entity(ServerPlayer owner,SpellDefinition spell,Vec3 pos,Vec3 direction) {
		var entity=new PhaseTwoEntity(MagicSystem.PHASE_TWO,owner.level()); entity.configure(owner,spell,pos,direction); return entity;
	}
	public static boolean execute(ServerPlayer player,SpellDefinition spell,MagicAim direction) {
		var level=(ServerLevel)player.level();
		switch(spell.id()) {
			case "ex_healing": player.heal(spell.healingAmount(player.getHealth(),player.getMaxHealth())); return true;
			case "antidote_ii": return player.removeEffect(MobEffects.POISON)|player.removeEffect(MobEffects.NAUSEA)|player.removeEffect(MobEffects.HUNGER);
			case "wind_bind","ice_pillar","ice_lance": return instant(player,spell,direction);
			case "explosion": return level.addFreshEntity(entity(player,spell,player.getEyePosition(),direction.direction()));
			case "wind_blast": {
				launch(player,PhaseTwoRules.launchVelocity(direction.direction().scale(-1),6));
				// Refresh a fixed duration; never add to the remaining effect time.
				player.addEffect(new MobEffectInstance(MobEffects.LEVITATION,20,0)); return true;
			}
			case "earth_lance": {
				var hit=aim(player,direction);
				if(hit!=null&&hit.getType()==HitResult.Type.BLOCK&&hit.getDirection()==net.minecraft.core.Direction.UP&&PhaseTwoRules.underfoot(hit.getBlockPos(),player.blockPosition())) {
					return construct(player,spell,direction);
				}
				return instant(player,spell,direction);
			}
			case "ice_fortress": return construct(player,spell,direction);
			case "physical_shield": return PhaseTwoRuntime.shield(player,direction);
			case "flame_pillar": {
				var hit=trace(player,direction,spell.range());if(hit==null)return false;

				if(hit.target()!=null)return PhaseTwoRuntime.igniteTarget(player,hit.target(),direction);
				var block=aim(player,direction);if(block==null||block.getType()!=HitResult.Type.BLOCK)return false;
				return igniteBlock(player,block);
			}
			case "earth_pillar": {
				var hit=trace(player,direction,12);var block=aim(player,direction);if(block==null)return false;
				var point=hit!=null?Vec3.atBottomCenterOf(hit.base()):block.getLocation();
				var top=point.add(0,6,0);
				var bounds=new AABB(top.x-1.5,top.y,top.z-1.5,top.x+1.5,top.y+3,top.z+1.5);
				if(!loaded(level,bounds))return false;
				for(var pos:BlockPos.betweenClosed(BlockPos.containing(bounds.minX,bounds.minY,bounds.minZ),BlockPos.containing(bounds.maxX-1.0E-6,bounds.maxY-1.0E-6,bounds.maxZ-1.0E-6)))if(!level.getBlockState(pos).isAir())return false;
				return level.addFreshEntity(entity(player,spell,top,new Vec3(0,-1,0)));
			}
			case "icicle_break": return level.addFreshEntity(entity(player,spell,player.getEyePosition(),direction.direction()));
			case "flood_flush": return level.addFreshEntity(entity(player,spell,player.getEyePosition(),direction.direction()));
			default: return level.addFreshEntity(entity(player,spell,player.getEyePosition(),direction.direction()));
		}
	}
	public static void damage(PhaseTwoEntity carrier,LivingEntity target) {
		var spell=carrier.spell(); var owner=carrier.getOwner(); var level=(ServerLevel)carrier.level();
		if(!MagicEffects.allowed(owner,target)||!target.hurtServer(level,MagicDamage.source(carrier,spell),spell.damage())) return;
		MagicEffects.apply(owner,target,spell); SpellHooks.afterHit.accept(carrier,target);
		if(spell==SpellDefinition.FLOOD_FLUSH) { var v=carrier.direction(); target.knockback(1.2,-v.x,-v.z); }
	}
	public static void areaDamage(PhaseTwoEntity carrier,Vec3 center,double half) {
		var box=new AABB(center.x-half,center.y-half,center.z-half,center.x+half,center.y+half,center.z+half);
		for(var entity:carrier.level().getEntities(carrier,box,e->e instanceof LivingEntity))
			if(IntermediateSpells.visible((ServerLevel)carrier.level(),center,(LivingEntity)entity,carrier)) damage(carrier,(LivingEntity)entity);
		((ServerLevel)carrier.level()).sendParticles(net.minecraft.core.particles.ParticleTypes.EXPLOSION,center.x,center.y,center.z,1,0,0,0,0);
	}
	public static List<BlockPos> plan(ServerPlayer player,SpellDefinition spell,MagicAim direction) {
		var hit=aim(player,direction); var result=new ArrayList<BlockPos>();
		boolean floatingWall=spell==SpellDefinition.ICE_FORTRESS;
		if(!floatingWall&&(hit==null||hit.getType()!=HitResult.Type.BLOCK||hit.getDirection()!=net.minecraft.core.Direction.UP)) return result;
		var start=floatingWall?BlockPos.containing(player.position().add(Vec3.directionFromRotation(0,direction.yaw()).scale(2))):hit.getBlockPos().above();
		boolean wall=spell==SpellDefinition.ICE_FORTRESS; boolean east=Math.abs(direction.direction().z)>=Math.abs(direction.direction().x);
		for(int y=0;y<3;y++) for(int x=wall?-2:0;x<=(wall?2:0);x++) result.add(start.offset(east?x:0,y,east?0:x));
		return result;
	}
	private static boolean preflight(ServerPlayer player,List<BlockPos> plan) {
		var level=(ServerLevel)player.level();
		for(var pos:plan) if(!MagicTerrain.mayChange(player,pos)||!level.getBlockState(pos).isAir()) return false;
		var bounds=new AABB(plan.getFirst()).minmax(new AABB(plan.getLast()));
		for(var entity:level.getEntities(player,bounds,e->e instanceof LivingEntity)) {
			var box=entity.getBoundingBox(); double lift=bounds.maxY-box.minY+0.02;
			if(!level.noCollision(entity,box.expandTowards(0,lift,0))) return false;
		}
		if(player.getBoundingBox().intersects(bounds)&&!level.noCollision(player,player.getBoundingBox().expandTowards(0,bounds.maxY-player.getY()+0.02,0))) return false;
		return true;
	}
	private static boolean construct(ServerPlayer player,SpellDefinition spell,MagicAim direction) {
		var plan=plan(player,spell,direction); if(plan.isEmpty()||!preflight(player,plan)) return false;
		var level=(ServerLevel)player.level(); var bounds=new AABB(plan.getFirst()).minmax(new AABB(plan.getLast()));
		boolean self=spell==SpellDefinition.EARTH_LANCE&&plan.getFirst().below().equals(player.blockPosition().below())&&player.getBoundingBox().intersects(bounds);
		var targets=new ArrayList<net.minecraft.world.entity.Entity>(level.getEntities(player,bounds,e->e instanceof LivingEntity));
		if(player.getBoundingBox().intersects(bounds)) targets.add(player);
		for(var target:targets) {
			launch(target,PhaseTwoRules.launchVelocity(new Vec3(0,1,0),3));
		}
		if(!self&&spell.damage()>0) {var carrier=entity(player,spell,Vec3.atCenterOf(plan.getFirst()),direction.direction());for(var target:targets) damage(carrier,(LivingEntity)target);}
		if(!targets.isEmpty()) { PhaseTwoRuntime.growingColumn(player,plan,spell,self); return true; }
		for(var pos:plan) {
			if(self) level.setBlockAndUpdate(pos,Blocks.DIRT.defaultBlockState());
			else MagicTerrain.temporary(player,pos,spell==SpellDefinition.EARTH_LANCE?MagicTerrain.TEMPORARY_DIRT:MagicTerrain.TEMPORARY_ICE,200);
		}
		if(!self&&spell.damage()>0) {
			var carrier=entity(player,spell,Vec3.atCenterOf(plan.getFirst()),direction.direction());
			for(var target:targets) damage(carrier,(LivingEntity)target);
		}
		return true;
	}
	public static boolean permanentStone(ServerPlayer owner,BlockPos center) {
		var level=(ServerLevel)owner.level(); var positions=new ArrayList<BlockPos>();
		for(var pos:BlockPos.betweenClosed(center.offset(-1,0,-1),center.offset(1,2,1))) {
			if(!MagicTerrain.mayChange(owner,pos)||!level.getBlockState(pos).isAir()) return false;
			positions.add(pos.immutable());
		}
		for(var pos:positions) level.setBlockAndUpdate(pos,Blocks.STONE.defaultBlockState()); return true;
	}
	public static void launch(net.minecraft.world.entity.Entity target,Vec3 motion) {
		target.setOnGround(false); target.setDeltaMovement(motion); target.hurtMarked=true;
		if(target instanceof ServerPlayer player) player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(player));
	}
	public record Ray(Vec3 point,BlockPos base,LivingEntity target) { }
	public static Ray trace(ServerPlayer player,MagicAim aim,double range) {
		return traceFrom(player,player.getEyePosition(),aim.direction(),range);
	}
	public static Ray traceFrom(ServerPlayer player,Vec3 from,Vec3 direction,double range) {
		var level=(ServerLevel)player.level();var end=from.add(direction.scale(range));
		if(!loaded(level,new AABB(from,end).inflate(1))) return null;
		var block=level.clip(new ClipContext(from,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,player));
		Vec3 point=block.getLocation(); LivingEntity target=null; double closest=from.distanceToSqr(point);
		for(var candidate:level.getEntities(player,new AABB(from,point).inflate(1),e->e instanceof LivingEntity&&MagicEffects.allowed(player,(LivingEntity)e))) {
			var intersect=candidate.getBoundingBox().clip(from,point);
			if(intersect.isPresent()&&from.distanceToSqr(intersect.get())<=closest) { point=intersect.get();closest=from.distanceToSqr(point);target=(LivingEntity)candidate; }
		}
		if(target!=null) return new Ray(point,BlockPos.containing(target.getX(),target.getBoundingBox().minY,target.getZ()),target);
		if(block.getType()!=HitResult.Type.BLOCK) return null;
		var base=block.getBlockPos().relative(block.getDirection());
		return new Ray(point,base,null);
	}

	private static boolean raiseAt(ServerPlayer player,SpellDefinition spell,MagicAim aim,BlockPos start,LivingEntity alreadyHit) {
		var plan=List.of(start,start.above(),start.above(2));if(!preflight(player,plan))return false;
		var level=(ServerLevel)player.level();var carrier=entity(player,spell,Vec3.atCenterOf(start),aim.direction());
		var bounds=new AABB(start).minmax(new AABB(start.above(2)));
		var targets=level.getEntities(player,bounds,e->e instanceof LivingEntity);
		for(var target:targets) {if(target!=alreadyHit)damage(carrier,(LivingEntity)target);launch(target,PhaseTwoRules.launchVelocity(new Vec3(0,1,0),3));}
		if(!targets.isEmpty()) PhaseTwoRuntime.growingColumn(player,plan,spell,false);
		else for(var pos:plan) MagicTerrain.temporary(player,pos,MagicTerrain.TEMPORARY_ICE,200);
		return true;
	}
	public static boolean instant(ServerPlayer player,SpellDefinition spell,MagicAim aim) {
		var hit=trace(player,aim,spell.range());if(hit==null)return false;
		if(spell==SpellDefinition.WATERFALL) {
			if(!MagicWaterSources.place(player,hit.base())&&!MagicWaterSources.renew(player,hit.base()))return false;
			PhaseTwoRuntime.waterContact(player,hit.base());return true;
		}
		if(spell==SpellDefinition.WIND_BIND&&(hit.target()==null||hit.target().getType().builtInRegistryHolder().is(BIND_IMMUNE)))return false;
		if(spell==SpellDefinition.EARTH_LANCE&&hit.target()==null)return false;
		if(spell==SpellDefinition.ICE_PILLAR)return raiseAt(player,spell,aim,hit.base(),null);
		var carrier=entity(player,spell,hit.point(),aim.direction());
		if(spell==SpellDefinition.ICE_LANCE)return player.level().addFreshEntity(entity(player,spell,Vec3.atBottomCenterOf(hit.base()),aim.direction()));
		if(spell==SpellDefinition.EARTH_LANCE){
			if(!MagicSpikes.raise(player,hit.target().blockPosition()))return false;
			damage(carrier,hit.target());return true;
		}
		if(spell==SpellDefinition.WIND_BIND)return hit.target().addEffect(new MobEffectInstance(MagicEffects.WIND_BOUND,100,0),player);
		return false;
	}

	/** Any targeted face is accepted; fall back to the block top when fire needs solid support. */
	private static boolean igniteBlock(ServerPlayer player,BlockHitResult hit) {
		var level=(ServerLevel)player.level();
		for(var pos:List.of(hit.getBlockPos().relative(hit.getDirection()),hit.getBlockPos().above())) {
			if(!MagicTerrain.mayChange(player,pos))continue;
			if(level.getBlockState(pos).is(Blocks.FIRE)||level.getBlockState(pos).is(Blocks.SOUL_FIRE))return true;
			if(!level.getBlockState(pos).isAir())continue;
			var fire=net.minecraft.world.level.block.BaseFireBlock.getState(level,pos);
			if(fire.canSurvive(level,pos)&&level.setBlockAndUpdate(pos,fire))return true;
		}
		return false;
	}

	private PhaseTwoSpells() { }
}
