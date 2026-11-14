package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.application.port.SelfEffect;
import com.example.examplemod.magic.spell.detox.antidote_ii.AntidoteIIEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
public record AntidoteIIAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return AntidoteIIEffect.apply(new AntidoteIIEffect.Patient(){public boolean removePoison(){return player.removeEffect(MobEffects.POISON);}public boolean removeNausea(){return player.removeEffect(MobEffects.NAUSEA);}public boolean removeHunger(){return player.removeEffect(MobEffects.HUNGER);}});}
}
