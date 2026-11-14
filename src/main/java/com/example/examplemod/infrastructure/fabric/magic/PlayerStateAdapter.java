package com.example.examplemod.infrastructure.fabric.magic;

import net.minecraft.server.level.ServerPlayer;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.magic.application.port.PlayerStore;

public record PlayerStateAdapter(ServerPlayer player) implements PlayerStore {
	public PlayerMagicState read() { return MagicSystem.state(player).pure(); }
	public void write(PlayerMagicState state) { player.setAttached(MagicAttachments.STATE, MagicState.from(state)); }
}
