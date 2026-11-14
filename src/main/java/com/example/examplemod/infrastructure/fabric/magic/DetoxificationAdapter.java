package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.detox.detoxification.DetoxificationEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
public record DetoxificationAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return DetoxificationEffect.apply(()->player.removeEffect(MobEffects.POISON));}
}
