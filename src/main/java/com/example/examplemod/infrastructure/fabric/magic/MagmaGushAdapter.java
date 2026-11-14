package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.EarthHedgehogAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.fire.magma_gush.*;
import com.example.examplemod.magic.application.port.SelfEffect;

import java.util.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.*;
import net.minecraft.world.level.storage.*;

/** Eruption at zero, then exactly eight one-second residual pulses. */
public record MagmaGushAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return cast(player,new MagicAim(yaw,pitch));}
	private static final class Runtime {
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,MagmaGushState>> TYPE =
		net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("magma_gush_adapter_runtime"),
			builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,MagmaGushState>()));
	}

	/** The level owns this transient index; original explicit ValueInput/ValueOutput remains authoritative. */
	private static java.util.Map<MagicAreaEntity,MagmaGushState> states(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.TYPE);
	}
	public static float damageAt(long age){return MagmaGushRules.damage(age);}
	public static boolean contains(Vec3 offset){return MagmaGushRules.contains(offset.x,offset.y,offset.z);}
	public static boolean cast(ServerPlayer owner,MagicAim aim){var at=EarthHedgehogAdapter.target(owner,aim);if(at==null)return false;var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,owner.level());area.configure(owner,SpellDefinition.MAGMA_GUSH,aim);area.setPos(at);var s=new MagmaGushState(owner.level().getGameTime(),-1);states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,s.start());if(!AreaRuntimeAdapter.replaceExpansion(owner,area)){states(area).remove(area);return false;}TerrainLeaseRepository.scorch(owner,at);tick(area);SpellVisuals.burst((ServerLevel)owner.level(),SpellDefinition.MAGMA_GUSH,at,new Vec3(0,1,0));return true;}
	public static void tick(MagicAreaEntity area){
		if(!(area.level() instanceof ServerLevel level))return;var s=states(area).get(area);
		MagmaGushTick.tick(new MagmaGushTick.World(){
			ServerPlayer owner;long age;
			public boolean statePresent(){return s!=null;}
			public boolean ownerPresent(){if(area.getOwner() instanceof ServerPlayer p){owner=p;return true;}return false;}
			public boolean alive(){return owner.isAlive();}
			public boolean sameLevel(){return owner.level()==level;}
			public boolean expired(){return level.getGameTime()-s.start()>160;}
			public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner,area);}
			public void removeState(){states(area).remove(area);}
			public void discard(){area.discard();}
			public boolean begin(){age=level.getGameTime()-s.start();return s.begin(age);}
			public float damage(){return damageAt(age);}
			public void hitTargets(float damage){
				for(var e:level.getEntities(area,area.areaBounds(),e->e instanceof LivingEntity&&e.isAlive()&&!e.isSpectator())){
					var target=(LivingEntity)e;
					MagmaGushHit.apply(new MagmaGushHit.Target(){
						public boolean inside(){return contains(target.getBoundingBox().getCenter().subtract(area.position()));}
						public boolean owner(){return target==owner;}
						public boolean allowed(){return MagicEffects.allowed(owner,target);}
						public boolean visible(){return SpellVisibilityAdapter.visible(level,area.position().add(0,.3,0),target,area);}
						public boolean claim(){return MagicFieldCadence.claim(target,"fire",20);}
						public boolean hurt(){return target.hurtServer(level,MagicDamage.source(area,SpellDefinition.MAGMA_GUSH),damage);}
						public void selfEffects(){if(target.getRandom().nextFloat()<.8F&&!target.fireImmune()&&!target.hasEffect(net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE))target.igniteForTicks(300);}
						public void otherEffects(){MagicEffects.apply(owner,target,SpellDefinition.MAGMA_GUSH);}
						public void afterHit(){SpellHooks.afterHit(area,target);}
					});
				}
			}
		});
	}
	public static void save(MagicAreaEntity area,ValueOutput out){var s=states(area).get(area);if(s==null)return;out.putLong("MagmaStart",s.start());out.putInt("MagmaProcessed",s.processed());}
	public static void load(MagicAreaEntity area,ValueInput in){var s=new MagmaGushState(in.getLongOr("MagmaStart",Long.MIN_VALUE/2),in.getIntOr("MagmaProcessed",160));states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,s.start());}
}
