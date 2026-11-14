package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.water.squall.*;
import com.example.examplemod.magic.application.port.SelfEffect;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/** Local rain: no dimension weather changes, fluid placement or chunk loading. */
public record SquallAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return cast(player,new MagicAim(yaw,pitch));}
	private static final class Runtime {
		private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,Long>> EXPIRY =
			net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("squall_adapter_runtime"),
				builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,Long>()));
	}
	private static java.util.Map<MagicAreaEntity,Long> expiry(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.EXPIRY);
	}
	public static Vec3 target(ServerPlayer owner, MagicAim aim) {
		var level=(ServerLevel)owner.level();var from=owner.getEyePosition();var direction=aim.direction();
		for(int i=0;i<48;i++){
			var a=from.add(direction.scale(i));var b=from.add(direction.scale(i+1));
			if(!SpellRaycastAdapter.loaded(level,new net.minecraft.world.phys.AABB(a,b)))return null;
			var hit=level.clip(new ClipContext(a,b,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,owner));
			if(hit.getType()==net.minecraft.world.phys.HitResult.Type.BLOCK)return hit.getLocation().subtract(direction.scale(.02));
		}
		return from.add(direction.scale(48));
	}
	public static boolean cast(ServerPlayer owner,MagicAim aim) {
		var at=target(owner,aim); if(at==null)return false;
		var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,owner.level());
		area.configure(owner,SpellDefinition.SQUALL,aim); area.setPos(at);
		boolean evaporates=owner.level().environmentAttributes().getValue(net.minecraft.world.attribute.EnvironmentAttributes.WATER_EVAPORATES,BlockPos.containing(at));
		expiry(area).put(area,owner.level().getGameTime()+SquallRules.duration(evaporates));
		if(!AreaRuntimeAdapter.replaceExpansion(owner,area)){expiry(area).remove(area);return false;}
		apply(area);return true;
	}
	public static void tick(MagicAreaEntity area) {
		if(!(area.level() instanceof ServerLevel level))return;
		var owner=area.getOwner() instanceof ServerPlayer p?p:null;
		SquallTick.tick(new SquallTick.World(){
			public boolean ownerPresent(){return owner!=null;}
			public boolean alive(){return owner.isAlive();}
			public boolean removed(){return owner.isRemoved();}
			public boolean sameLevel(){return owner.level()==level;}
			public boolean expired(){return level.getGameTime()>=expiry(area).getOrDefault(area,0L);}
			public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner,area);}
			public void removeState(){expiry(area).remove(area);}
			public void discard(){area.discard();}
			public int age(){return area.tickCount;}
			public void apply(){SquallAdapter.apply(area);}
		});
	}
	private static void apply(MagicAreaEntity area) {
		var level=(ServerLevel)area.level(); var owner=(ServerPlayer)area.getOwner();
		for(var entity:level.getEntities(area,area.areaBounds(),e->e instanceof LivingEntity&&e.isAlive()&&!e.isSpectator())){
			var target=(LivingEntity)entity; double dx=target.getX()-area.getX(),dz=target.getZ()-area.getZ();
			com.example.examplemod.magic.mechanic.field.RainExposure.apply(new com.example.examplemod.magic.mechanic.field.RainExposure.Target(){
				public boolean inside(){return SquallRules.inside(dx,dz);}
				public boolean loaded(){return level.hasChunkAt(target.blockPosition());}
				public boolean skyVisible(){return level.canSeeSky(BlockPos.containing(target.getEyePosition()));}
				public void extinguish(){target.clearFire();}
				public void wet(){target.addEffect(new MobEffectInstance(MagicEffects.WET,300,0),owner);}
			});
		}
	}
	public static void shorten(MagicAreaEntity area,int ticks){expiry(area).computeIfPresent(area,(a,end)->SquallRules.shorten(area.level().getGameTime(),end,ticks));}
	public static void save(MagicAreaEntity area,ValueOutput output){output.putLong("RainExpiry",expiry(area).getOrDefault(area,0L));}
	public static void load(MagicAreaEntity area,ValueInput input){expiry(area).put(area,input.getLongOr("RainExpiry",0));}
}
