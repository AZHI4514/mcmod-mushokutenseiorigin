package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.EarthHedgehogAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.mixed.quagmire.*;
import com.example.examplemod.magic.application.port.SelfEffect;

import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.level.storage.*;

/** A surface effect, never a fluid or a downward teleport. */
public record QuagmireAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return cast(player,new MagicAim(yaw,pitch));}
	private static final class Runtime {
		private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,Long>> EXPIRY =
			net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("quagmire_adapter_runtime"),
				builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,Long>()));
	}
	private static java.util.Map<MagicAreaEntity,Long> expiry(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.EXPIRY);
	}
	public static boolean contains(Vec3 offset){return QuagmireRules.contains(offset.x,offset.y,offset.z);}
	public static double jumpVelocity(double upward){return QuagmireRules.jumpVelocity(upward);}
	public static boolean cast(ServerPlayer owner,MagicAim aim){var at=EarthHedgehogAdapter.target(owner,aim);if(at==null)return false;var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,owner.level());area.configure(owner,SpellDefinition.QUAGMIRE,aim);area.setPos(at.add(0,.025,0));expiry(area).put(area,owner.level().getGameTime()+400);if(!AreaRuntimeAdapter.replaceExpansion(owner,area)){expiry(area).remove(area);return false;}tick(area);return true;}
	public static void tick(MagicAreaEntity area){
		if(!(area.level() instanceof ServerLevel level))return;
		QuagmireTick.tick(new QuagmireTick.World(){
			ServerPlayer owner;
			public boolean ownerPresent(){if(area.getOwner() instanceof ServerPlayer p){owner=p;return true;}return false;}
			public boolean alive(){return owner.isAlive();}
			public boolean sameLevel(){return owner.level()==level;}
			public boolean expired(){return level.getGameTime()>=expiry(area).getOrDefault(area,0L);}
			public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner,area);}
			public void removeState(){expiry(area).remove(area);}
			public void discard(){area.discard();}
			public void contacts(){
				for(var e:level.getEntities(area,area.areaBounds(),e->e instanceof LivingEntity&&e.isAlive()&&!e.isSpectator())){
					var target=(LivingEntity)e;
					QuagmireContact.apply(new QuagmireContact.Target(){
						public boolean inside(){return contains(target.position().subtract(area.position()));}
						public boolean owner(){return target==owner;}
						public boolean allowed(){return MagicEffects.allowed(owner,target);}
						public boolean visible(){return SpellVisibilityAdapter.visible(level,area.position().add(0,.2,0),target,area);}
						public boolean immune(){return MagicControl.immune(target);}
						public void slow(boolean boss){target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,4,boss?0:2),owner);}
						public void hold(){target.addEffect(new MobEffectInstance(MagicEffects.MUD_HELD,2,0),owner);}
					});
				}
			}
		});
	}
	public static void save(MagicAreaEntity area,ValueOutput out){out.putLong("MudExpiry",expiry(area).getOrDefault(area,0L));}
	public static void load(MagicAreaEntity area,ValueInput in){expiry(area).put(area,in.getLongOr("MudExpiry",0));}
}
