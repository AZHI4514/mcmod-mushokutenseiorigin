package com.example.examplemod.infrastructure.fabric.magic;
import net.minecraft.server.level.ServerPlayer;
import com.example.examplemod.magic.application.port.SelfEffect;
/** Vanilla heal remains authoritative, including a zero amount in the compatibility entry. */
public final class ExHealingAdapter implements SelfEffect {
	private final ServerPlayer player;
	public ExHealingAdapter(ServerPlayer player){this.player=player;}
	public boolean apply(float yaw,float pitch){
		return com.example.examplemod.magic.spell.healing.ex_healing.ExHealingEffect.apply(new com.example.examplemod.magic.spell.healing.ex_healing.ExHealingEffect.Patient(){
			public float health(){return player.getHealth();}
			public float maximum(){return player.getMaxHealth();}
			public void heal(float amount){player.heal(amount);}
		});
	}
}
