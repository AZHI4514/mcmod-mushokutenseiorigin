package com.example.examplemod.magic;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.projectile.Projectile;
import com.example.examplemod.ExampleMod;

/** 火焰伤害复用原版标签与抗火，附加状态统一由MagicEffects在成功伤害后处理。 */
public final class MagicDamage {
	public static DamageSource source(Projectile projectile, SpellDefinition spell) {
		if (!spell.element().equals("fire")) return projectile.damageSources().thrown(projectile, projectile.getOwner());
		var level = (ServerLevel) projectile.level();
		String id = spell.execution() == SpellDefinition.Execution.FLAME_SLICE ? "flame_slice" : "basic_fireball";
		return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(
				ResourceKey.create(Registries.DAMAGE_TYPE, ExampleMod.id(id))), projectile, projectile.getOwner());
	}

	private MagicDamage() {
	}
}
