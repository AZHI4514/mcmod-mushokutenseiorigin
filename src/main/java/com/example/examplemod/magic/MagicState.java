package com.example.examplemod.magic;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** 不可变的玩家存档。没有客户端写权限，计时器离线不增长。 */
public record MagicState(int mana, int maximum, int onlineTicks, int cooldown,
		List<String> learned, List<String> loaded, String selected) {
	public static final int RECOVERY_TICKS = 24000;
	public static final int MAX_SLOTS = 10;
	public static final Codec<MagicState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("mana").forGetter(MagicState::mana),
			Codec.INT.fieldOf("maximum").forGetter(MagicState::maximum),
			Codec.INT.fieldOf("online_ticks").forGetter(MagicState::onlineTicks),
			Codec.INT.fieldOf("cooldown").forGetter(MagicState::cooldown),
			Codec.STRING.listOf().fieldOf("learned").forGetter(MagicState::learned),
			Codec.STRING.listOf().fieldOf("loaded").forGetter(MagicState::loaded),
			Codec.STRING.fieldOf("selected").forGetter(MagicState::selected)
	).apply(instance, MagicState::new));

	public MagicState {
		maximum = Math.clamp(maximum, 1, 1000000);
		mana = Math.clamp(mana, 0, maximum);
		onlineTicks = Math.clamp(onlineTicks, 0, RECOVERY_TICKS - 1);
		cooldown = Math.max(0, cooldown);
		learned = learned.stream().filter(id -> SpellDefinition.find(id) != null).distinct().toList();
		List<String> known = learned;
		loaded = loaded.stream().filter(known::contains).distinct().limit(MAX_SLOTS).toList();
		if (!loaded.contains(selected)) selected = loaded.isEmpty() ? "" : loaded.getFirst();
	}

	public static MagicState initial(int maximum) {
		return new MagicState(maximum, maximum, 0, 0, List.of(), List.of(), "");
	}

	public MagicState tick() {
		if (onlineTicks + 1 >= RECOVERY_TICKS) return new MagicState(maximum, maximum, 0, Math.max(0, cooldown - 1), learned, loaded, selected);
		return new MagicState(mana, maximum, onlineTicks + 1, Math.max(0, cooldown - 1), learned, loaded, selected);
	}

	public MagicState refill() {
		return new MagicState(maximum, maximum, 0, cooldown, learned, loaded, selected);
	}

	/** 作弊恢复只修改当前魔力，不重置自然恢复计时或冷却。 */
	public MagicState restoreMana(int amount) {
		int restored = (int) Math.min(maximum, mana + (long) Math.max(0, amount));
		return new MagicState(restored, maximum, onlineTicks, cooldown, learned, loaded, selected);
	}

	public MagicState withMaximum(int value) {
		return new MagicState(mana, value, onlineTicks, cooldown, learned, loaded, selected);
	}

	public boolean canCast(SpellDefinition spell) {
		return spell != null && spell.id().equals(selected) && loaded.contains(spell.id())
				&& learned.contains(spell.id()) && cooldown == 0 && mana >= spell.manaCost();
	}

	public MagicState spendMana(int amount) {
		if (amount < 0 || amount > mana) throw new IllegalArgumentException("Invalid mana debit");
		return new MagicState(mana-amount,maximum,onlineTicks,cooldown,learned,loaded,selected);
	}
	public MagicState withCooldown(int ticks) {
		return new MagicState(mana,maximum,onlineTicks,ticks,learned,loaded,selected);
	}
	public MagicState cast(SpellDefinition spell) {
		if (!canCast(spell)) throw new IllegalStateException("Invalid cast");
		return new MagicState(mana - spell.manaCost(), maximum, onlineTicks,
				spell.cooldownTicks(), learned, loaded, selected);
	}

	public MagicState learn(String id) {
		var spell = SpellDefinition.find(id);
		if (spell == null || learned.contains(id) || !spell.learningRequirementsMet(learned)) return this;
		List<String> next = new ArrayList<>(learned);
		next.add(id);
		List<String> slots = new ArrayList<>(loaded);
		if (slots.size() < MAX_SLOTS) slots.add(id);
		return new MagicState(mana, maximum, onlineTicks, cooldown, next, slots, selected);
	}

	public MagicState toggle(String id) {
		if (!learned.contains(id)) return this;
		List<String> slots = new ArrayList<>(loaded);
		if (slots.contains(id)) slots.remove(id);
		else if (slots.size() < MAX_SLOTS) slots.add(id);
		return new MagicState(mana, maximum, onlineTicks, cooldown, learned, slots, selected);
	}

	public MagicState select(String id) {
		if (!loaded.contains(id)) return this;
		return new MagicState(mana, maximum, onlineTicks, cooldown, learned, loaded, id);
	}
}
