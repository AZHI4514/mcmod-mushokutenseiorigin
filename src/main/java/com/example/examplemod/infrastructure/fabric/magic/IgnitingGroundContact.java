package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.*;
public record IgnitingGroundContact(int fireTicks) implements GroundContactEffect {
	public void afterDamage(MagicAreaEntity area,ServerPlayer owner,LivingEntity target,long expiry,long now) {
		if(target.getRandom().nextFloat()<.8F&&!target.fireImmune()&&!target.hasEffect(MobEffects.FIRE_RESISTANCE))target.igniteForTicks(fireTicks);
	}
}
