package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.*;
/** World side-effect Strategy; pure hit/claim decisions belong to GroundFieldPolicy. */
public interface GroundContactEffect {
	default void continuous(ServerPlayer owner,LivingEntity target) {}
	void afterDamage(MagicAreaEntity area,ServerPlayer owner,LivingEntity target,long expiry,long now);
}
