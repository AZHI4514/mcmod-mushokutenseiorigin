package com.example.examplemod.infrastructure.fabric.magic;
import java.util.Map;
import java.util.UUID;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.spell.water.water_shield.WaterShieldContact;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
public final class WaterShieldContactAdapter {
	public static void apply(ServerPlayer owner,LivingEntity target,SpellDefinition spell,Map<UUID,Integer> contacts,int pulse){
		WaterShieldContact.apply(new WaterShieldContact.Target(){
			public void drag(){var motion=target.getDeltaMovement();target.setDeltaMovement(motion.multiply(0.8,0.8,0.8));}
			public void markMotion(){target.hurtMarked=true;}
			public boolean alreadyContacted(){return contacts.containsKey(target.getUUID());}
			public void claim(){contacts.put(target.getUUID(),pulse);}
			public void wet(){MagicEffects.apply(owner,target,spell);}
		});
	}
	private WaterShieldContactAdapter() { }
}
