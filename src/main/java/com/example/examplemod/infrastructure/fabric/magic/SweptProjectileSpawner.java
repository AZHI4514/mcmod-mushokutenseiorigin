package com.example.examplemod.infrastructure.fabric.magic;

import com.example.examplemod.magic.*;
import net.minecraft.server.level.ServerPlayer;

public final class SweptProjectileSpawner {
	public static SweptSpellProjectile projectile(ServerPlayer owner,SpellDefinition spell,MagicAim aim,int paid) {
		var projectile=new SweptSpellProjectile(MagicSystem.intermediateType(spell),owner.level());
		projectile.setOwner(owner); projectile.paidMana(paid); projectile.setPos(owner.getEyePosition());
		var direction=aim.direction(); projectile.shoot(direction.x,direction.y,direction.z,spell.speed(),0); return projectile;
	}
	public static boolean launch(ServerPlayer owner,SpellDefinition spell,MagicAim aim,int paid) {
		return owner.level().addFreshEntity(projectile(owner,spell,aim,paid));
	}
	private SweptProjectileSpawner(){}
}
