package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.WindFieldRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.fire.inferno.*;
import com.example.examplemod.magic.mechanic.field.*;
import com.example.examplemod.magic.application.port.SelfEffect;

import java.util.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.*;

/** Initial flash and eight residual pulses use distinct clocks. */
public record InfernoAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return cast(player,new MagicAim(yaw,pitch));}
	private static final class Runtime {
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,BurstFieldSchedule>> TYPE =
		net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("inferno_adapter_runtime"),
			builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,BurstFieldSchedule>()));
	}

	/** The level owns this transient index; original explicit ValueInput/ValueOutput remains authoritative. */
	private static java.util.Map<MagicAreaEntity,BurstFieldSchedule> states(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.TYPE);
	}
	public static float residual(long age){return InfernoRules.residual(age);}
	public static boolean contains(Vec3 p){return InfernoRules.contains(p.x,p.y,p.z);}
	public static boolean cast(ServerPlayer owner,MagicAim aim){var at=WindFieldRuntimeAdapter.target(owner,SpellDefinition.INFERNO,aim);if(at==null)return false;var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,owner.level());area.configure(owner,SpellDefinition.INFERNO,aim);area.setPos(at);long start=owner.level().getGameTime();var s=new BurstFieldSchedule(start,start+401,start+20,false);states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,s.start());if(!AreaRuntimeAdapter.replaceExpansion(owner,area)){states(area).remove(area);return false;}TerrainLeaseRepository.scorch(owner,at,20,2048,401);tick(area);SpellVisuals.burst((ServerLevel)owner.level(),SpellDefinition.INFERNO,at,new Vec3(0,1,0));return true;}
	public static void tick(MagicAreaEntity area){
		if(!(area.level() instanceof ServerLevel level))return;var s=states(area).get(area);long now=level.getGameTime();
		ExpiringFieldTick.tick(new ExpiringFieldTick.World(){
			ServerPlayer owner;
			public boolean statePresent(){return s!=null;}public boolean expired(){return now>=s.expiry();}
			public boolean ownerPresent(){if(area.getOwner() instanceof ServerPlayer p){owner=p;return true;}return false;}
			public boolean alive(){return owner.isAlive();}public boolean sameLevel(){return owner.level()==level;}public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner,area);}
			public void removeState(){states(area).remove(area);}public void discard(){area.discard();}
			public void update(){var frame=s.begin(now);if(!frame.active())return;
				for(var e:level.getEntities(area,area.areaBounds(),e->e instanceof LivingEntity&&e.isAlive()&&!e.isSpectator())){
					var target=(LivingEntity)e;
					BurstFieldContact.apply(frame.flash(),frame.pulse(),new BurstFieldContact.Target(){
						public boolean inside(){return contains(target.getBoundingBox().getCenter().subtract(area.position()));}
						public boolean owner(){return target==owner;}public boolean allowed(){return MagicEffects.allowed(owner,target);}
						public boolean visible(){return SpellVisibilityAdapter.visible(level,area.position().add(0,.2,0),target,area);}
						public void flash(){hit(level,area,owner,target,18);}public boolean claim(){return MagicFieldCadence.claim(target,"fire",20);}public void residual(){hit(level,area,owner,target,5);}
					});
				}
			}
		});
	}
	private static void hit(ServerLevel level,MagicAreaEntity area,ServerPlayer owner,LivingEntity target,float damage){if(!target.hurtServer(level,MagicDamage.source(area,SpellDefinition.INFERNO),damage))return;if(target==owner){if(target.getRandom().nextFloat()<.8F&&!target.fireImmune()&&!target.hasEffect(net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE))target.igniteForTicks(600);}else MagicEffects.apply(owner,target,SpellDefinition.INFERNO);SpellHooks.afterHit(area,target);}
	public static void shorten(MagicAreaEntity area,int ticks){var s=states(area).get(area);if(s!=null)s.shorten(area.level().getGameTime(),ticks);}
	public static void save(MagicAreaEntity area,ValueOutput out){var s=states(area).get(area);if(s==null)return;out.putLong("InfernoStart",s.start());out.putLong("InfernoExpiry",s.expiry());out.putLong("InfernoNext",s.next());out.putBoolean("Infernoed",s.flashed());}
	public static void load(MagicAreaEntity area,ValueInput in){var s=new BurstFieldSchedule(in.getLongOr("InfernoStart",0),in.getLongOr("InfernoExpiry",0),in.getLongOr("InfernoNext",Long.MAX_VALUE),in.getBooleanOr("Infernoed",true));states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,s.start());}
}
