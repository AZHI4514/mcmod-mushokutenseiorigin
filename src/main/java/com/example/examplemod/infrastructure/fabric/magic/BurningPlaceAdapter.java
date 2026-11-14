package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import java.util.*;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.fire.burning_place.BurningPlaceContact;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
public record BurningPlaceAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return AreaRuntimeAdapter.cast(player,SpellDefinition.BURNING_PLACE,new MagicAim(yaw,pitch));}
	public static void contact(MagicAreaEntity carrier,ServerPlayer owner,ServerLevel level,LivingEntity target,Map<UUID,Integer> contacts,int pulse){
		BurningPlaceContact.apply(new BurningPlaceContact.Target(){
			public boolean alreadyContacted(){return contacts.containsKey(target.getUUID());}
			public boolean touches(){return carrier.getOwner() instanceof LivingEntity body&&MagicRules.touchesBody(body.getBoundingBox(),target.getBoundingBox());}
			public boolean allowed(){return MagicEffects.allowed(owner,target);}
			public boolean visible(){return level.clip(new ClipContext(carrier.position().add(0,1,0),target.getBoundingBox().getCenter(),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,carrier)).getType()==HitResult.Type.MISS;}
			public void claim(){contacts.put(target.getUUID(),pulse);}
			public boolean hurt(){return target.hurtServer(level,MagicDamage.source(carrier,carrier.spell()),carrier.spell().damage());}
			public void effects(){MagicEffects.apply(owner,target,carrier.spell());}
			public void afterHit(){SpellHooks.afterHit(carrier,target);}
		});
	}
}
