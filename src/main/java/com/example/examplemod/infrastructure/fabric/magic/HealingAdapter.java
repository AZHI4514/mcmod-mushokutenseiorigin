package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.healing.healing.HealingEffect;
import net.minecraft.server.level.*;
public record HealingAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return HealingEffect.apply(new HealingEffect.Patient(){
		public float health(){return player.getHealth();}public float maximum(){return player.getMaxHealth();}public void heal(float amount){player.heal(amount);}
		public void visual(){((ServerLevel)player.level()).sendParticles(SpellParticles.burst(SpellDefinition.HEALING),player.getX(),player.getY()+1,player.getZ(),8,.3,.4,.3,0);}
	},SpellDefinition.HEALING.healing());}
}
