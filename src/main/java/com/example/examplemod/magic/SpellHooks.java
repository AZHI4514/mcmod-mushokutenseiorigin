package com.example.examplemod.magic;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

/** 未来依赖与命中状态扩展点；本批不启用疾病、湿润或燃烧。服务端线程调用。 */
public final class SpellHooks {
	public enum CastSource { KEYBOARD, WAND }
	public static BiPredicate<ServerPlayer, String> requirement = (player, id) -> false;
	public static BiConsumer<net.minecraft.world.entity.projectile.Projectile, Entity> afterHit = (projectile, target) -> { };

	public static boolean canUse(ServerPlayer player, SpellDefinition spell, CastSource source) {
		return source == CastSource.KEYBOARD && (spell.castMode() == SpellDefinition.CastMode.INSTANT || PhaseTwoSpells.isChannel(spell))
				&& spell.prerequisitesMet(id -> requirement.test(player, id));
	}

	private SpellHooks() {
	}
}
