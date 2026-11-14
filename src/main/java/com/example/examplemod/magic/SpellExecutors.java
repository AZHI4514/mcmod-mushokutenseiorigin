package com.example.examplemod.magic;
import net.minecraft.server.level.ServerPlayer;
/** Compatibility entry point; dispatch is an explicitly validated immutable registry. */
public final class SpellExecutors {
	public static boolean canExecute(ServerPlayer player,SpellDefinition spell,MagicAim aim) {
		return SpellExecutionBindings.canExecute(player,spell,aim);
	}
	public static boolean execute(ServerPlayer player,SpellDefinition spell,MagicAim aim) {
		return SpellExecutionBindings.execute(player,spell,aim);
	}
	private SpellExecutors() {}
}
