package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.*;
public final class TerraFallGroundContact implements GroundContactEffect {
	public void afterDamage(MagicAreaEntity area,ServerPlayer owner,LivingEntity target,long expiry,long now) {
		target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,(int)Math.max(100,expiry-now+100),2),owner);
	}
}
