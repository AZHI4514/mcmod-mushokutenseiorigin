package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.*;
public final class EarthquakeGroundContact implements GroundContactEffect {
	public void afterDamage(MagicAreaEntity area,ServerPlayer owner,LivingEntity target,long expiry,long now) {
		if(!MagicControl.immune(target)){target.setDeltaMovement(target.getDeltaMovement().add(0,.45,0));target.hurtMarked=true;}
		MagicControl.apply(target,target instanceof Player?15:30);
	}
}
