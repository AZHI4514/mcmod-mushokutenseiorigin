package com.example.examplemod.magic.domain.player;

import com.example.examplemod.magic.catalog.LearningCatalog;

import java.util.ArrayList;
import java.util.List;


/** 不可变的玩家存档。没有客户端写权限，计时器离线不增长。 */
public record PlayerMagicState(int mana, int maximum, int onlineTicks, int cooldown,
		List<String> learned, List<String> loaded, String selected, LearningCatalog catalog) {
	public static final int RECOVERY_TICKS = 24000;
	public static final int MAX_SLOTS = 10;

	public PlayerMagicState {
		maximum = ManaPolicy.maximum(maximum);
		mana = ManaPolicy.balance(mana, maximum);
		onlineTicks = Math.clamp(onlineTicks, 0, RECOVERY_TICKS - 1);
		cooldown = Math.max(0, cooldown);
		learned = learned.stream().filter(id -> catalog.contains(id)).distinct().toList();
		List<String> known = learned;
		loaded = loaded.stream().filter(known::contains).distinct().limit(MAX_SLOTS).toList();
		if (!loaded.contains(selected)) selected = loaded.isEmpty() ? "" : loaded.getFirst();
	}

	public static PlayerMagicState initial(int maximum, LearningCatalog catalog) {
		return new PlayerMagicState(maximum, maximum, 0, 0, List.of(), List.of(), "", catalog);
	}

	public PlayerMagicState tick() {
		if (onlineTicks + 1 >= RECOVERY_TICKS) return new PlayerMagicState(maximum, maximum, 0, Math.max(0, cooldown - 1), learned, loaded, selected, catalog);
		return new PlayerMagicState(mana, maximum, onlineTicks + 1, Math.max(0, cooldown - 1), learned, loaded, selected, catalog);
	}

	public PlayerMagicState refill() {
		return new PlayerMagicState(maximum, maximum, 0, cooldown, learned, loaded, selected, catalog);
	}

	/** 作弊恢复只修改当前魔力，不重置自然恢复计时或冷却。 */
	public PlayerMagicState restoreMana(int amount) {
		int restored = ManaPolicy.restore(mana, maximum, amount);
		return new PlayerMagicState(restored, maximum, onlineTicks, cooldown, learned, loaded, selected, catalog);
	}

	public PlayerMagicState withMaximum(int value) {
		return new PlayerMagicState(mana, value, onlineTicks, cooldown, learned, loaded, selected, catalog);
	}

	public boolean canCast(String id, int manaCost) {
		return catalog.contains(id) && id.equals(selected) && loaded.contains(id)
				&& learned.contains(id) && cooldown == 0 && mana >= manaCost;
	}

	public PlayerMagicState spendMana(int amount) {
		if (amount < 0 || amount > mana) throw new IllegalArgumentException("Invalid mana debit");
		return new PlayerMagicState(mana-amount,maximum,onlineTicks,cooldown,learned,loaded,selected,catalog);
	}
	public PlayerMagicState withCooldown(int ticks) {
		return new PlayerMagicState(mana,maximum,onlineTicks,ticks,learned,loaded,selected,catalog);
	}
	public PlayerMagicState cast(String id, int manaCost, int recoveryTicks) {
		if (!canCast(id, manaCost)) throw new IllegalStateException("Invalid cast");
		return new PlayerMagicState(mana - manaCost, maximum, onlineTicks,
				recoveryTicks, learned, loaded, selected, catalog);
	}

	public PlayerMagicState learn(String id) {
		var spell = catalog.find(id).orElse(null);
		if (spell == null || learned.contains(id) || !learned.containsAll(spell.prerequisites())) return this;
		List<String> next = new ArrayList<>(learned);
		next.add(id);
		List<String> slots = new ArrayList<>(loaded);
		if (slots.size() < MAX_SLOTS) slots.add(id);
		return new PlayerMagicState(mana, maximum, onlineTicks, cooldown, next, slots, selected, catalog);
	}

	/** Compatibility books can teach every implemented spell without prerequisite checks. */
	public PlayerMagicState learnWithoutRequirements(String id) {
		var spell = catalog.find(id).orElse(null);
		if (spell == null || learned.contains(id)) return this;
		List<String> next = new ArrayList<>(learned);
		next.add(id);
		List<String> slots = new ArrayList<>(loaded);
		if (slots.size() < MAX_SLOTS) slots.add(id);
		return new PlayerMagicState(mana, maximum, onlineTicks, cooldown, next, slots, selected, catalog);
	}

	public PlayerMagicState toggle(String id) {
		if (!learned.contains(id)) return this;
		List<String> slots = new ArrayList<>(loaded);
		if (slots.contains(id)) slots.remove(id);
		else if (slots.size() < MAX_SLOTS) slots.add(id);
		return new PlayerMagicState(mana, maximum, onlineTicks, cooldown, learned, slots, selected, catalog);
	}

	public PlayerMagicState select(String id) {
		if (!loaded.contains(id)) return this;
		return new PlayerMagicState(mana, maximum, onlineTicks, cooldown, learned, loaded, id, catalog);
	}
}
