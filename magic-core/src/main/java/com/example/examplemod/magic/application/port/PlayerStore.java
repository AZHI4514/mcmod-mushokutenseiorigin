package com.example.examplemod.magic.application.port;

import com.example.examplemod.magic.domain.player.PlayerMagicState;

/** Bound to one player by the composition root; never stores a global current player. */
public interface PlayerStore {
	PlayerMagicState read();
	void write(PlayerMagicState state);
}
