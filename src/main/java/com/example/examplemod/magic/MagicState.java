package com.example.examplemod.magic;

import java.util.List;
import com.mojang.serialization.Codec;
import com.example.examplemod.bootstrap.MagicCatalogs;
import com.example.examplemod.magic.domain.player.PlayerMagicState;
import com.example.examplemod.infrastructure.fabric.persistence.MagicStateCodec;

/** Compatibility transport record; all state transitions are delegated to the pure core. */
public record MagicState(int mana, int maximum, int onlineTicks, int cooldown,
		List<String> learned, List<String> loaded, String selected) {
	public static final int RECOVERY_TICKS = PlayerMagicState.RECOVERY_TICKS;
	public static final int MAX_SLOTS = PlayerMagicState.MAX_SLOTS;
	public static final Codec<MagicState> CODEC = MagicStateCodec.CODEC;
	public MagicState {
		var pure = new PlayerMagicState(mana, maximum, onlineTicks, cooldown, learned, loaded, selected, MagicCatalogs.LEARNING);
		mana = pure.mana(); maximum = pure.maximum(); onlineTicks = pure.onlineTicks(); cooldown = pure.cooldown();
		learned = pure.learned(); loaded = pure.loaded(); selected = pure.selected();
	}
	public PlayerMagicState pure() { return new PlayerMagicState(mana, maximum, onlineTicks, cooldown, learned, loaded, selected, MagicCatalogs.LEARNING); }
	public static MagicState from(PlayerMagicState s) { return new MagicState(s.mana(), s.maximum(), s.onlineTicks(), s.cooldown(), s.learned(), s.loaded(), s.selected()); }
	public static MagicState initial(int maximum) { return from(PlayerMagicState.initial(maximum, MagicCatalogs.LEARNING)); }
	public MagicState tick() { return from(pure().tick()); }
	public MagicState refill() { return from(pure().refill()); }
	public MagicState restoreMana(int amount) { return from(pure().restoreMana(amount)); }
	public MagicState withMaximum(int value) { return from(pure().withMaximum(value)); }
	public boolean canCast(SpellDefinition spell) { return spell != null && pure().canCast(spell.id(), spell.manaCost()); }
	public MagicState spendMana(int amount) { return from(pure().spendMana(amount)); }
	public MagicState withCooldown(int ticks) { return from(pure().withCooldown(ticks)); }
	public MagicState cast(SpellDefinition spell) {
		if (!canCast(spell)) throw new IllegalStateException("Invalid cast");
		return from(pure().cast(spell.id(), spell.manaCost(), spell.cooldownTicks()));
	}
	public MagicState learn(String id) { return from(pure().learn(id)); }
	public MagicState learnWithoutRequirements(String id) { return from(pure().learnWithoutRequirements(id)); }
	public MagicState toggle(String id) { return from(pure().toggle(id)); }
	public MagicState select(String id) { return from(pure().select(id)); }
}
