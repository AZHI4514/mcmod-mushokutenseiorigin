package com.example.examplemod.infrastructure.fabric.magic;
import net.minecraft.server.level.ServerPlayer;
import com.example.examplemod.magic.MagicAim;
import com.example.examplemod.magic.SpellDefinition;
import com.example.examplemod.magic.application.port.PaidProjectile;
/** Adds one already-paid projectile. Mana and cooldown are exclusively ChargePayment's responsibility. */
public record StoneCannonProjectileAdapter(ServerPlayer player, MagicAim aim) implements PaidProjectile {
	@Override public boolean spawn(int paid) {
		return player.level().addFreshEntity(SweptProjectileSpawner.projectile(player,SpellDefinition.STONE_CANNON,aim,paid));
	}
}
