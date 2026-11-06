package com.example.examplemod.magic;

import java.util.List;
import com.mojang.serialization.JsonOps;

/** 无需启动游戏的行为测试，由 verifyMagic 运行并纳入 check/build。 */
public final class MagicStateTest {
	private static int assertions;
	private static void check(boolean condition, String message) {
		assertions++;
		if (!condition) throw new AssertionError(message);
	}

	public static void main(String[] args) {
		var spell = SpellDefinition.WATER_BALL;
		var state = MagicState.initial(100);
		check(!state.canCast(spell), "Unknown spell cannot cast");
		state = state.learn(spell.id());
		check(state.canCast(spell), "First learning loads and selects spell");
		check(state.learn(spell.id()).equals(state), "Learning is idempotent");
		var paid = state.cast(spell);
		check(paid.mana() == 99 && paid.cooldown() == 2, "One successful cast debits exactly once");
		check(!paid.canCast(spell), "Immediate second cast denied");
		check(!paid.tick().canCast(spell), "One tick is insufficient");
		check(paid.tick().tick().canCast(spell), "Two tick cooldown");
		try { paid.cast(spell); throw new AssertionError("Cooldown bypass"); }
		catch (IllegalStateException expected) { assertions++; }
		var zero = new MagicState(0, 100, 0, 0, state.learned(), state.loaded(), state.selected());
		check(!zero.canCast(spell), "Insufficient mana denied");
		check(!state.select("").selected().isEmpty(), "Blank selection keeps previous");
		var removed = state.toggle(spell.id());
		check(removed.selected().isEmpty() && removed.loaded().isEmpty() && removed.learned().size() == 1, "Unload does not forget");
		check(removed.toggle(spell.id()).canCast(spell), "Known spell can be loaded again");
		check(paid.withMaximum(200).mana() == 99, "Increasing maximum does not refill");
		check(paid.withMaximum(50).mana() == 50, "Decreasing maximum clamps mana");
		check(paid.withMaximum(200).maximum() == 200, "Maximum editable");
		var countdown = new MagicState(3, 100, 23998, 2, state.learned(), state.loaded(), state.selected());
		check(countdown.tick().mana() == 3, "No early daily recovery");
		var recovered = countdown.tick().tick();
		check(recovered.mana() == 100 && recovered.onlineTicks() == 0 && recovered.cooldown() == 0, "Daily recovery at exactly 24000 ticks");
		check(paid.refill().learned().equals(paid.learned()) && paid.refill().loaded().equals(paid.loaded())
				&& paid.refill().selected().equals(paid.selected()), "Recovery preserves knowledge and selection");
		var dirty = new MagicState(-5, -1, -10, -2, List.of("unknown", spell.id(), spell.id()), List.of("unknown", spell.id(), spell.id()), "unknown");
		check(dirty.mana() == 0 && dirty.maximum() == 1 && dirty.onlineTicks() == 0 && dirty.cooldown() == 0, "Invalid numeric data sanitized");
		check(dirty.learned().size() == 1 && dirty.loaded().size() == 1 && dirty.selected().equals(spell.id()), "Invalid and duplicate spells sanitized");
		var json = MagicState.CODEC.encodeStart(JsonOps.INSTANCE, paid).getOrThrow();
		check(MagicState.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow().equals(paid), "Persistent codec round trip");
		var day = zero;
		for (int i = 0; i < 23999; i++) day = day.tick();
		check(day.mana() == 0, "No premature full day recovery");
		check(day.tick().mana() == 100, "Full online day refills");
		var continuous = state;
		int successes = 0;
		for (int tick = 0; tick < 200; tick++) {
			if (continuous.canCast(spell)) { continuous = continuous.cast(spell); successes++; }
			continuous = continuous.tick();
		}
		check(successes == 100 && continuous.mana() == 0, "100 casts consume 100 mana without overspending");
		check(zero.restoreMana(30).mana() == 30, "Cheat restores requested amount");
		check(paid.restoreMana(30).mana() == 100, "Cheat recovery clamps to maximum");
		check(zero.restoreMana(Integer.MAX_VALUE).mana() == 100, "Full restore cannot overflow");
		check(paid.restoreMana(1).cooldown() == paid.cooldown() && paid.restoreMana(1).onlineTicks() == paid.onlineTicks(), "Cheat preserves timers");
		check(paid.restoreMana(-1).equals(paid), "Negative recovery never removes mana");
		check(paid.restoreMana(1).learned().equals(paid.learned()) && paid.restoreMana(1).selected().equals(paid.selected()), "Cheat preserves spell state");

		var four = MagicState.initial(100);
		for (var definition : SpellDefinition.ALL) four = four.learn(definition.id());
		check(four.learned().size() == 4 && four.loaded().size() == 4, "Four lessons load distinct slots");
		check(four.selected().equals(spell.id()), "Later lessons preserve selection");
		for (var definition : SpellDefinition.ALL) {
			var ready = four.select(definition.id());
			var spent = ready.cast(definition);
			check(spent.mana() == 100 - definition.manaCost(), "Exact cost: " + definition.id());
			for (int i = 1; i < definition.cooldownTicks(); i++) {
				spent = spent.tick();
				check(!spent.canCast(definition), "No early cooldown: " + definition.id());
			}
			check(spent.tick().canCast(definition), "Cooldown ends exactly: " + definition.id());
			var poor = new MagicState(definition.manaCost() - 1, 100, 0, 0, four.learned(), four.loaded(), definition.id());
			check(!poor.canCast(definition), "Reject insufficient mana: " + definition.id());
			check(ready.learn(definition.id()).equals(ready), "No duplicate lesson: " + definition.id());
			check(definition.prerequisitesMet(id -> false), "Basic spell has no external dependency");
		}
		var firePaid = four.select("fireball").cast(SpellDefinition.FIREBALL).select("water_ball");
		check(!firePaid.canCast(spell), "Switching cannot bypass global cooldown");
		var fourJson = MagicState.CODEC.encodeStart(JsonOps.INSTANCE, firePaid).getOrThrow();
		check(MagicState.CODEC.parse(JsonOps.INSTANCE, fourJson).getOrThrow().equals(firePaid), "Four spells persist with cooldown");
		check(four.toggle("water_ball").selected().equals("fireball"), "Removing selection falls back to first remaining");
		var gated = new SpellDefinition("future", 1, 1, 2, 100, "water", 1.5F, 0.03, 0,
				SpellDefinition.CastMode.CHANT, List.of("disease_system"));
		check(!gated.prerequisitesMet(id -> false), "Missing future dependency denies availability");
		check(gated.prerequisitesMet(id -> id.equals("disease_system")), "Known dependency can be supplied later");
		System.out.println("MagicState: " + assertions + " behavior assertions passed.");
	}
}
