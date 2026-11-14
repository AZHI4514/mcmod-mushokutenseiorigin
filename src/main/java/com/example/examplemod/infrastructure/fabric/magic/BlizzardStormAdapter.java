package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.water.blizzard_storm.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.IntSupplier;
public record BlizzardStormAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return AreaRuntimeAdapter.cast(player,SpellDefinition.BLIZZARD_STORM,new MagicAim(yaw,pitch));}
	public static void tick(MagicAreaEntity area,ServerLevel level,IntSupplier pulse,Runnable advance){
		var owner=area.getOwner() instanceof ServerPlayer p?p:null;
		BlizzardStormTick.tick(new BlizzardStormTick.World(){
			public boolean ownerPresent(){return owner!=null;}
			public boolean alive(){return owner.isAlive();}
			public boolean removed(){return owner.isRemoved();}
			public boolean sameLevel(){return owner.level()==level;}
			public int pulse(){return pulse.getAsInt();}
			public void discard(){area.discard();}
			public void follow(){area.setPos(owner.position());}
			public boolean adopt(){return AreaRuntimeAdapter.adoptExpansion(owner,area);}
			public void advance(){advance.run();}
			public void hitTargets(){
				var center=owner.position().add(0,1,0);
				for(var entity:level.getEntities(area,area.areaBounds(),e->e instanceof LivingEntity)){
					var target=(LivingEntity)entity;
					if(!MagicEffects.allowed(owner,target)||!BlizzardStormRules.within(target.getBoundingBox().getCenter().distanceToSqr(center))||!SpellVisibilityAdapter.visible(level,center,target,area))continue;
					if(MagicFieldCadence.claim(target,"water",10)&&target.hurtServer(level,MagicDamage.source(area,area.spell()),area.spell().damage())){MagicEffects.apply(owner,target,area.spell());SpellHooks.afterHit(area,target);}
				}
			}
		});
	}
}
