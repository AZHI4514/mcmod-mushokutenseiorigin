package com.example.examplemod.magic.spell.earth.stone_cannon;
/** World facts captured by the adapter; no player or registry dependency. */
public record ChargeEligibility(boolean alive, boolean spectator, boolean usingItem, boolean sleeping,
		boolean inventoryMenu, boolean selected, boolean loaded, boolean learned) {
	public boolean eligible() {
		return alive && !spectator && !usingItem && !sleeping && inventoryMenu && selected && loaded && learned;
	}
}
