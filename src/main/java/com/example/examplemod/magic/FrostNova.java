package com.example.examplemod.magic;
import net.minecraft.server.level.ServerPlayer;
/** Compatibility entry; the world implementation lives in the Fabric adapter. */
public final class FrostNova {
	public static boolean cast(ServerPlayer owner,MagicAim aim){
		return com.example.examplemod.infrastructure.fabric.magic.FrostNovaAdapter.cast(owner,aim);
	}
	private FrostNova() { }
}
