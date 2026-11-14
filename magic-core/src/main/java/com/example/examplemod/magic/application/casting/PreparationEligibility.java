package com.example.examplemod.magic.application.casting;

/** Lazy preparation eligibility; expensive/event-backed checks remain last. */
public final class PreparationEligibility {
	public interface Player {
		boolean counterReady();
		boolean silenced();
		boolean alive();
		boolean spectator();
		boolean usingItem();
		boolean sleeping();
		boolean inventoryOpen();
		boolean cooldownReady();
		boolean selected();
		boolean learned();
		boolean loaded();
		boolean enoughMana();
		boolean hookAllows();
	}
	public static boolean allows(Player player) {
		return player.counterReady()&&!player.silenced()&&player.alive()&&!player.spectator()&&!player.usingItem()&&!player.sleeping()
				&&player.inventoryOpen()&&player.cooldownReady()&&player.selected()&&player.learned()&&player.loaded()&&player.enoughMana()&&player.hookAllows();
	}
	private PreparationEligibility() { }
}
