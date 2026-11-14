package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.WindFieldRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.water.absolute_zero.*;
import com.example.examplemod.magic.mechanic.field.*;
import com.example.examplemod.magic.application.port.SelfEffect;

import java.util.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.*;

/** One freezing shock, followed by status-only cold mist. */
public record AbsoluteZeroAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return cast(player,new MagicAim(yaw,pitch));}
	private static final class Runtime {
	private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<java.util.Map<MagicAreaEntity,BurstFieldSchedule>> TYPE =
		net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(com.example.examplemod.ExampleMod.id("absolute_zero_adapter_runtime"),
			builder -> builder.initializer(() -> new java.util.WeakHashMap<MagicAreaEntity,BurstFieldSchedule>()));
	}

	/** The level owns this transient index; original explicit ValueInput/ValueOutput remains authoritative. */
	private static java.util.Map<MagicAreaEntity,BurstFieldSchedule> states(MagicAreaEntity area) {
		return area.level().getAttachedOrCreate(Runtime.TYPE);
	}
	public static boolean contains(Vec3 p){return AbsoluteZeroRules.contains(p.x,p.y,p.z);}
	public static boolean cast(ServerPlayer owner,MagicAim aim){var at=WindFieldRuntimeAdapter.target(owner,SpellDefinition.ABSOLUTE_ZERO,aim);if(at==null)return false;var area=new MagicAreaEntity(MagicSystem.MAGIC_AREA,owner.level());area.configure(owner,SpellDefinition.ABSOLUTE_ZERO,aim);area.setPos(at);long start=owner.level().getGameTime();var s=new BurstFieldSchedule(start,start+160,start+20,false);states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,s.start());if(!AreaRuntimeAdapter.replaceExpansion(owner,area)){states(area).remove(area);return false;}TerrainLeaseRepository.freezeSurface(owner,at,16,4096,1200);tick(area);SpellVisuals.burst((ServerLevel)owner.level(),SpellDefinition.ABSOLUTE_ZERO,at,new Vec3(0,1,0));return true;}
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
					AbsoluteZeroContact.apply(frame.flash(),new AbsoluteZeroContact.Target(){
						public boolean inside(){return contains(target.getBoundingBox().getCenter().subtract(area.position()));}
						public boolean owner(){return target==owner;}public boolean allowed(){return MagicEffects.allowed(owner,target);}public boolean visible(){return SpellVisibilityAdapter.visible(level,area.position(),target,area);}
						public boolean warm(){return target.hasEffect(MagicEffects.WARMTH);}public boolean wet(){return target.hasEffect(MagicEffects.WET);}
						public boolean hurt(float damage){return target.hurtServer(level,MagicDamage.source(area,SpellDefinition.ABSOLUTE_ZERO),damage);}
						public void removeWet(){target.removeEffect(MagicEffects.WET);}public void removeWarm(){target.removeEffect(MagicEffects.WARMTH);}public boolean canFreeze(){return target.canFreeze();}
						public void freeze(){target.addEffect(new MobEffectInstance(MagicEffects.FROZEN,600,0),owner);}public void control(){MagicControl.apply(target,AbsoluteZeroRules.controlTicks(target instanceof Player),MagicEffects.ICE_BOUND);}public void afterHit(){SpellHooks.afterHit(area,target);}public boolean randomFreeze(){return target.getRandom().nextFloat()<.8F;}
					});
				}
			}
		});
	}
	public static void save(MagicAreaEntity area,ValueOutput out){var s=states(area).get(area);if(s==null)return;out.putLong("ZeroStart",s.start());out.putLong("ZeroExpiry",s.expiry());out.putLong("ZeroNext",s.next());out.putBoolean("ZeroReleased",s.flashed());}
	public static void load(MagicAreaEntity area,ValueInput in){var s=new BurstFieldSchedule(in.getLongOr("ZeroStart",0),in.getLongOr("ZeroExpiry",0),in.getLongOr("ZeroNext",Long.MAX_VALUE),in.getBooleanOr("ZeroReleased",true));states(area).put(area,s);area.setAttached(MagicAttachments.SPIKES_STARTED,s.start());}
}
