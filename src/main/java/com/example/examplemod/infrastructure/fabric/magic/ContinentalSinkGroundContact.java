package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.*;
public final class ContinentalSinkGroundContact implements GroundContactEffect {
	public void continuous(ServerPlayer owner,LivingEntity target){target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS,25,2),owner);}
	public void afterDamage(MagicAreaEntity area,ServerPlayer owner,LivingEntity target,long expiry,long now) {
		if(!MagicControl.immune(target)){
			var inward=area.position().subtract(target.position()).multiply(1,0,1).normalize().scale(.5);
			target.setDeltaMovement(target.getDeltaMovement().add(inward));target.hurtMarked=true;
		}
	}
}
