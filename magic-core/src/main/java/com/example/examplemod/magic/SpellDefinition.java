package com.example.examplemod.magic;

import java.util.List;

/** 服务端参数的单一来源。未接入的施法方式与前置不能被当作可用技能。 */
public record SpellDefinition(String id, int manaCost, float damage, int cooldownTicks,
		int lifetimeTicks, String element, float speed, double gravity, double knockback,
		CastMode castMode, List<String> requirements, Execution execution, float healing,
		double range, int maxTargets, List<String> learningRequirements, int timingRank, boolean officiallyGraded, CastingProfile explicitProfile) {
	/** Compatibility constructor preserves all existing spell definitions. */
	public SpellDefinition(String id, int manaCost, float damage, int cooldownTicks, int lifetimeTicks,
			String element, float speed, double gravity, double knockback, CastMode castMode,
			List<String> requirements, Execution execution, float healing, double range, int maxTargets,
			List<String> learningRequirements) {
		this(id, manaCost, damage, cooldownTicks, lifetimeTicks, element, speed, gravity, knockback,
				castMode, requirements, execution, healing, range, maxTargets, learningRequirements,
				legacyRank(execution), true, null);
	}
	private static int legacyRank(Execution execution) {
		return execution == Execution.ADVANCED_PROJECTILE || execution == Execution.TARGET_AREA
				|| execution == Execution.PHASE_TWO ? 2 : 1;
	}
	public int officialRank() { return officiallyGraded ? timingRank : 0; }
	public enum Execution { ILLUSION, TELEPORT, COMMUNICATION, CORE_FIELD, SUMMON, UTILITY, EXPANSION_BARRIER, MODE_FIELD, SOUND, SUPPORT, SMOKE_FIELD, EXPANSION_FIELD, OCEAN_FREEZE, HOLY, ABSOLUTE_ZERO, INFERNO, FLASH_OVER, WIND_FIELD, MAGMA_GUSH, THERMAL_GUST, QUAGMIRE, EARTH_HEDGEHOG, EARTH_FORTRESS, EARTH_WALL, SONIC_BLAST, FROST_NOVA, THUNDER, PROJECTILE, FLAME_SLICE, SELF_HEAL, AREA, HEAT_HAND, DETOX, ADVANCED_PROJECTILE, TARGET_AREA, PHASE_TWO }
	public enum CastMode { INSTANT, CHANT, CHARGE, CHANNEL }
	public static final SpellDefinition WATER_BALL = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("water_ball");
	public static final SpellDefinition FIREBALL = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("fireball");
	public static final SpellDefinition WIND_BALL = com.example.examplemod.magic.domain.spec.catalog.WindSpellDefinitions.create("wind_ball");
	public static final SpellDefinition ROCK_BULLET = com.example.examplemod.magic.domain.spec.catalog.EarthSpellDefinitions.create("rock_bullet");
	public static final SpellDefinition WATER_ARROW = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("water_arrow");
	public static final SpellDefinition ICE_SMASH = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("ice_smash");
	public static final SpellDefinition FLAME_SLICE = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("flame_slice");
	public static final SpellDefinition HEALING = com.example.examplemod.magic.domain.spec.catalog.HealingSpellDefinitions.create("healing");
	public static final SpellDefinition WATER_SHIELD = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("water_shield");
	public static final SpellDefinition ICE_BLADE = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("ice_blade");
	public static final SpellDefinition HEAT_HAND = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("heat_hand");
	public static final SpellDefinition HEAT_ISLAND = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("heat_island");
	public static final SpellDefinition BURNING_PLACE = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("burning_place");
	public static final SpellDefinition DETOXIFICATION = com.example.examplemod.magic.domain.spec.catalog.HealingSpellDefinitions.create("detoxification");
	public static final SpellDefinition MAGIC_SHIELD = com.example.examplemod.magic.domain.spec.catalog.MagicSpellDefinitions.create("magic_shield");
	public static final SpellDefinition WATER_CANNON = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("water_cannon");
	public static final SpellDefinition WATER_SPLASH = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("water_splash");
	public static final SpellDefinition ICICLE_FIELD = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("icicle_field");
	public static final SpellDefinition EX_FLAME = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("ex_flame");
	public static final SpellDefinition SONIC_BOOM = com.example.examplemod.magic.domain.spec.catalog.WindSpellDefinitions.create("sonic_boom");
	public static final SpellDefinition STONE_CANNON = com.example.examplemod.magic.domain.spec.catalog.EarthSpellDefinitions.create("stone_cannon");

	public static final SpellDefinition ICE_PILLAR = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("ice_pillar");
	public static final SpellDefinition WATERFALL = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("waterfall");
	public static final SpellDefinition ICE_LANCE = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("ice_lance");
	public static final SpellDefinition ICICLE_BREAK = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("icicle_break");
	public static final SpellDefinition ICICLE_EDGE = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("icicle_edge");
	public static final SpellDefinition FLOOD_FLUSH = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("flood_flush");
	public static final SpellDefinition ICE_FORTRESS = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("ice_fortress");
	public static final SpellDefinition EXPLOSION = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("explosion");
	public static final SpellDefinition FLAME_PILLAR = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("flame_pillar");
	public static final SpellDefinition FLAMETHROWER = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("flamethrower");
	public static final SpellDefinition WIND_SLICE = com.example.examplemod.magic.domain.spec.catalog.WindSpellDefinitions.create("wind_slice");
	public static final SpellDefinition WIND_BIND = com.example.examplemod.magic.domain.spec.catalog.WindSpellDefinitions.create("wind_bind");
	public static final SpellDefinition WIND_BLAST = com.example.examplemod.magic.domain.spec.catalog.WindSpellDefinitions.create("wind_blast");
	public static final SpellDefinition EARTH_LANCE = com.example.examplemod.magic.domain.spec.catalog.EarthSpellDefinitions.create("earth_lance");
	public static final SpellDefinition EARTH_PILLAR = com.example.examplemod.magic.domain.spec.catalog.EarthSpellDefinitions.create("earth_pillar");
	public static final SpellDefinition EX_HEALING = com.example.examplemod.magic.domain.spec.catalog.HealingSpellDefinitions.create("ex_healing");
	public static final SpellDefinition ANTIDOTE_II = com.example.examplemod.magic.domain.spec.catalog.HealingSpellDefinitions.create("antidote_ii");
	public static final SpellDefinition PHYSICAL_SHIELD = com.example.examplemod.magic.domain.spec.catalog.MagicSpellDefinitions.create("physical_shield");
	public static final SpellDefinition FLAME_TORNADO = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("flame_tornado");
	public static final SpellDefinition EXORCIST_RAY = com.example.examplemod.magic.domain.spec.catalog.HolySpellDefinitions.create("exorcist_ray");
	public static final SpellDefinition EXORCIST_BURST = com.example.examplemod.magic.domain.spec.catalog.HolySpellDefinitions.create("exorcist_burst");
	public static final SpellDefinition EXORCIST_JUDGE = com.example.examplemod.magic.domain.spec.catalog.HolySpellDefinitions.create("exorcist_judge");
	public static final SpellDefinition HOLY_RAY = com.example.examplemod.magic.domain.spec.catalog.HolySpellDefinitions.create("holy_ray");
	public static final SpellDefinition SACRED_NOVA = com.example.examplemod.magic.domain.spec.catalog.HolySpellDefinitions.create("sacred_nova");
	public static final SpellDefinition DIVINE_JUDGE = com.example.examplemod.magic.domain.spec.catalog.HolySpellDefinitions.create("divine_judge");
	public static final SpellDefinition GOD_PURGE = com.example.examplemod.magic.domain.spec.catalog.HolySpellDefinitions.create("god_purge");
	public static final SpellDefinition OCEAN_FREEZE = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("ocean_freeze");
	public static final SpellDefinition FLAME_STORM = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("flame_storm");
	public static final SpellDefinition APOCALYPSE_FLAME = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("apocalypse_flame");
	public static final SpellDefinition EARTHQUAKE = com.example.examplemod.magic.domain.spec.catalog.EarthSpellDefinitions.create("earthquake");
	public static final SpellDefinition TERRA_FALL = com.example.examplemod.magic.domain.spec.catalog.EarthSpellDefinitions.create("terra_fall");
	public static final SpellDefinition CONTINENTAL_SINK = com.example.examplemod.magic.domain.spec.catalog.EarthSpellDefinitions.create("continental_sink");
	public static final SpellDefinition SAND_CLOUD = com.example.examplemod.magic.domain.spec.catalog.EarthSpellDefinitions.create("sand_cloud");
	public static final SpellDefinition DUST_STORM = com.example.examplemod.magic.domain.spec.catalog.EarthSpellDefinitions.create("dust_storm");
	public static final SpellDefinition SANDSTORM = com.example.examplemod.magic.domain.spec.catalog.EarthSpellDefinitions.create("sandstorm");
	public static final SpellDefinition DEEP_MIST = com.example.examplemod.magic.domain.spec.catalog.MixedSpellDefinitions.create("deep_mist");
	public static final SpellDefinition MUD_RAIN = com.example.examplemod.magic.domain.spec.catalog.MixedSpellDefinitions.create("mud_rain");
	public static final SpellDefinition ANTIDOTE_III = com.example.examplemod.magic.domain.spec.catalog.HealingSpellDefinitions.create("antidote_iii");
	public static final SpellDefinition HOWLING = com.example.examplemod.magic.domain.spec.catalog.SpecialSpellDefinitions.create("howling");
	public static final SpellDefinition HYPNOSIS_MAGIC = com.example.examplemod.magic.domain.spec.catalog.SpecialSpellDefinitions.create("hypnosis_magic");
	public static final SpellDefinition ATMOSPHERE_DOMINION = com.example.examplemod.magic.domain.spec.catalog.WindSpellDefinitions.create("atmosphere_dominion");
	public static final SpellDefinition GRAVITY_MAGIC = com.example.examplemod.magic.domain.spec.catalog.SpecialSpellDefinitions.create("gravity_magic");
	public static final SpellDefinition NUCLEAR_EXPLOSION = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("nuclear_explosion");
	public static final SpellDefinition DUAL_SHIELD = com.example.examplemod.magic.domain.spec.catalog.MagicSpellDefinitions.create("dual_shield");
	public static final SpellDefinition SPIRIT_SEALING_BARRIER = com.example.examplemod.magic.domain.spec.catalog.MagicSpellDefinitions.create("spirit_sealing_barrier");
	public static final SpellDefinition CONFINEMENT_BARRIER = com.example.examplemod.magic.domain.spec.catalog.MagicSpellDefinitions.create("confinement_barrier");
	public static final SpellDefinition DISTURB_MAGIC = com.example.examplemod.magic.domain.spec.catalog.SpecialSpellDefinitions.create("disturb_magic");
	public static final SpellDefinition DIVINATION_MAGIC = com.example.examplemod.magic.domain.spec.catalog.SpecialSpellDefinitions.create("divination_magic");
	public static final SpellDefinition MONSTER_SUMMON = com.example.examplemod.magic.domain.spec.catalog.SummonSpellDefinitions.create("monster_summon");
	public static final SpellDefinition SPIRIT_SUMMON = com.example.examplemod.magic.domain.spec.catalog.SummonSpellDefinitions.create("spirit_summon");
	public static final SpellDefinition LAMPLIGHT_SPIRITS = com.example.examplemod.magic.domain.spec.catalog.SummonSpellDefinitions.create("lamplight_spirits");
	public static final SpellDefinition NECROMANCY = com.example.examplemod.magic.domain.spec.catalog.SpecialSpellDefinitions.create("necromancy");
	public static final SpellDefinition FORTRESS_BARRIER = com.example.examplemod.magic.domain.spec.catalog.MagicSpellDefinitions.create("fortress_barrier");
	public static final SpellDefinition DEMON_GOD_SEALING_BARRIER = com.example.examplemod.magic.domain.spec.catalog.MagicSpellDefinitions.create("demon_god_sealing_barrier");
	public static final SpellDefinition SAINT_DRAGON_BATTLE_AURA = com.example.examplemod.magic.domain.spec.catalog.SpecialSpellDefinitions.create("saint_dragon_battle_aura");
	public static final SpellDefinition COMMUNICATION_MAGIC = com.example.examplemod.magic.domain.spec.catalog.SpecialSpellDefinitions.create("communication_magic");
	public static final SpellDefinition TELEPORTATION_MAGIC = com.example.examplemod.magic.domain.spec.catalog.SpecialSpellDefinitions.create("teleportation_magic");
	public static final SpellDefinition ILLUSION_BARRIER = com.example.examplemod.magic.domain.spec.catalog.MagicSpellDefinitions.create("illusion_barrier");
	public static final SpellDefinition ABSOLUTE_ZERO = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("absolute_zero");
	public static final SpellDefinition INFERNO = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("inferno");
	public static final SpellDefinition FLASH_OVER = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("flash_over");
	public static final SpellDefinition VIOLENT_STORM = com.example.examplemod.magic.domain.spec.catalog.WindSpellDefinitions.create("violent_storm");
	public static final SpellDefinition TORNADO = com.example.examplemod.magic.domain.spec.catalog.WindSpellDefinitions.create("tornado");
	public static final SpellDefinition VACUUM_STORM = com.example.examplemod.magic.domain.spec.catalog.WindSpellDefinitions.create("vacuum_storm");
	public static final SpellDefinition SKY_TEMPEST = com.example.examplemod.magic.domain.spec.catalog.WindSpellDefinitions.create("sky_tempest");
	public static final SpellDefinition MAGMA_GUSH = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("magma_gush");
	public static final SpellDefinition BLAST = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("blast");
	public static final SpellDefinition BORA_BLAST = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("bora_blast");
	public static final SpellDefinition QUAGMIRE = com.example.examplemod.magic.domain.spec.catalog.MixedSpellDefinitions.create("quagmire");
	public static final SpellDefinition EARTH_HEDGEHOG = com.example.examplemod.magic.domain.spec.catalog.EarthSpellDefinitions.create("earth_hedgehog");
	public static final SpellDefinition EARTH_FORTRESS = com.example.examplemod.magic.domain.spec.catalog.EarthSpellDefinitions.create("earth_fortress");
	public static final SpellDefinition EARTH_WALL = com.example.examplemod.magic.domain.spec.catalog.EarthSpellDefinitions.create("earth_wall");
	public static final SpellDefinition SONIC_BLAST = com.example.examplemod.magic.domain.spec.catalog.WindSpellDefinitions.create("sonic_blast");
	public static final SpellDefinition FROST_NOVA = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("frost_nova");
	public static final SpellDefinition LIGHTNING = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("lightning");
	public static final SpellDefinition ELECTRIC = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("electric");
	public static final SpellDefinition CUMULONIMBUS = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("cumulonimbus");
	public static final SpellDefinition TORNADO_IMPACT = com.example.examplemod.magic.domain.spec.catalog.WindSpellDefinitions.create("tornado_impact");
	public static final SpellDefinition EXODUS_FLAME = com.example.examplemod.magic.domain.spec.catalog.FireSpellDefinitions.create("exodus_flame");
	public static final SpellDefinition SQUALL = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("squall");
	public static final SpellDefinition BLIZZARD_STORM = com.example.examplemod.magic.domain.spec.catalog.WaterSpellDefinitions.create("blizzard_storm");
	public static final List<SpellDefinition> ALL = List.of(WATER_BALL, FIREBALL, WIND_BALL, ROCK_BULLET,
			WATER_ARROW, ICE_SMASH, FLAME_SLICE, HEALING, WATER_SHIELD, ICE_BLADE, HEAT_HAND, HEAT_ISLAND, BURNING_PLACE, DETOXIFICATION, MAGIC_SHIELD, WATER_CANNON, WATER_SPLASH, ICICLE_FIELD, EX_FLAME, SONIC_BOOM, STONE_CANNON, ICE_PILLAR, WATERFALL, ICE_LANCE, ICICLE_BREAK, ICICLE_EDGE, FLOOD_FLUSH, ICE_FORTRESS, EXPLOSION, FLAME_PILLAR, FLAMETHROWER, WIND_SLICE, WIND_BIND, WIND_BLAST, EARTH_LANCE, EARTH_PILLAR, EX_HEALING, ANTIDOTE_II, PHYSICAL_SHIELD, BLIZZARD_STORM, SQUALL, EXODUS_FLAME, TORNADO_IMPACT, CUMULONIMBUS, LIGHTNING, ELECTRIC, FROST_NOVA, SONIC_BLAST, EARTH_WALL, EARTH_FORTRESS, EARTH_HEDGEHOG, QUAGMIRE, BLAST, BORA_BLAST, MAGMA_GUSH, VIOLENT_STORM, TORNADO, VACUUM_STORM, SKY_TEMPEST, FLASH_OVER, FLAME_TORNADO, INFERNO, ABSOLUTE_ZERO, EXORCIST_RAY, EXORCIST_BURST, EXORCIST_JUDGE, HOLY_RAY, SACRED_NOVA, DIVINE_JUDGE, GOD_PURGE, FLAME_STORM, APOCALYPSE_FLAME, EARTHQUAKE, TERRA_FALL, CONTINENTAL_SINK, SAND_CLOUD, DUST_STORM, SANDSTORM, DEEP_MIST, MUD_RAIN, ANTIDOTE_III, HOWLING, HYPNOSIS_MAGIC, ATMOSPHERE_DOMINION, GRAVITY_MAGIC, NUCLEAR_EXPLOSION, DUAL_SHIELD, SPIRIT_SEALING_BARRIER, CONFINEMENT_BARRIER, DISTURB_MAGIC, DIVINATION_MAGIC, MONSTER_SUMMON, SPIRIT_SUMMON, LAMPLIGHT_SPIRITS, NECROMANCY, FORTRESS_BARRIER, DEMON_GOD_SEALING_BARRIER, SAINT_DRAGON_BATTLE_AURA, COMMUNICATION_MAGIC, TELEPORTATION_MAGIC, ILLUSION_BARRIER, OCEAN_FREEZE);



	public enum Motion { BALLISTIC, CONSTANT, FALLING, INSTANT }
	public Motion motion(){return Motion.valueOf(com.example.examplemod.magic.domain.spec.SpellTraits.motion(id,execution==Execution.PROJECTILE).name());}
	public int flightTicks() { return motion() == Motion.CONSTANT ? com.example.examplemod.magic.domain.combat.EffectRules.flightTicks(rank()) : 0; }
	public double flightRange() { return motion() == Motion.CONSTANT ? speed * flightTicks() : Double.POSITIVE_INFINITY; }
	public boolean piercing() { return motion() == Motion.CONSTANT; }
	public boolean areaDamage(){return com.example.examplemod.magic.domain.spec.SpellTraits.areaDamage(id);}
	public int glowColor(){return com.example.examplemod.magic.domain.spec.SpellTraits.glowColor(id,element);}
	public CastingProfile castingProfile() { return explicitProfile; }
	/** Gameplay timing template; officialRank is separate. */
	public int rank() { return timingRank; }
	public int statusTicks(){return com.example.examplemod.magic.domain.spec.SpellTraits.statusTicks(id,rank());}
	public boolean icy(){return com.example.examplemod.magic.domain.spec.SpellTraits.icy(id);}

	/** 原有基础弹体定义保持相同参数与 ID。 */
	public SpellDefinition(String id, int manaCost, float damage, int cooldownTicks, int lifetimeTicks,
			String element, float speed, double gravity, double knockback, CastMode mode, List<String> requirements) {
		this(id, manaCost, damage, cooldownTicks, lifetimeTicks, element, speed, gravity, knockback,
				mode, requirements, Execution.PROJECTILE, 0, 0, 1, List.of());
	}

	public SpellDefinition {
		requirements = List.copyOf(requirements);
		learningRequirements = List.copyOf(learningRequirements);
		if (timingRank < 1 || timingRank > 7) throw new IllegalArgumentException(id);
		if (explicitProfile == null) explicitProfile = CastingProfile.of(timingRank, CastingProfile.Mode.FULL);
		cooldownTicks = explicitProfile.recoveryTicks();
		if (manaCost < 0 || damage < 0 || healing < 0 || range < 0 || maxTargets < 1 || cooldownTicks < 0 || lifetimeTicks < 1)
			throw new IllegalArgumentException("Invalid spell parameters: " + id);
	}



	/** 疾病、成长等未来条件接入点。未知依赖默认拒绝，不能空实现后直接放行。 */
	public boolean prerequisitesMet(java.util.function.Predicate<String> available) {
		return requirements.stream().allMatch(available);
	}

	public boolean learningRequirementsMet(List<String> learned) {
		return learned.containsAll(learningRequirements);
	}

	/** 与原版 heal 配合；满血、死亡或无效生命数值不产生治疗。 */
	public float healingAmount(float health, float maximum) {
		if(id.equals("ex_healing"))return com.example.examplemod.magic.spell.healing.ex_healing.ExHealingRules.amount(health,maximum);
		if(execution!=Execution.SELF_HEAL)return 0;
		return com.example.examplemod.magic.spell.healing.healing.HealingRules.amount(health,maximum,healing);
	}

	public static SpellDefinition find(String id) {
		return ALL.stream().filter(spell -> spell.id().equals(id)).findFirst().orElse(null);
	}

	/** GoF Builder: named optional construction, the immutable product validates effective values. */
	public static Builder builder(String id){return new Builder(id);}
	public static final class Builder {
		private final String id;
		private int manaCost=0;
		private float damage=0;
		private int cooldownTicks=0;
		private int lifetimeTicks=1;
		private String element="magic";
		private float speed=0;
		private double gravity=0;
		private double knockback=0;
		private CastMode castMode=CastMode.INSTANT;
		private List<String> requirements=List.of();
		private Execution execution=Execution.PROJECTILE;
		private float healing=0;
		private double range=0;
		private int maxTargets=1;
		private List<String> learningRequirements=List.of();
		private int timingRank=0;
		private boolean officiallyGraded=true;
		private CastingProfile explicitProfile=null;
		private Builder(String id){this.id=java.util.Objects.requireNonNull(id);}
		public Builder manaCost(int value){this.manaCost=value;return this;}
		public Builder damage(float value){this.damage=value;return this;}
		public Builder cooldownTicks(int value){this.cooldownTicks=value;return this;}
		public Builder lifetimeTicks(int value){this.lifetimeTicks=value;return this;}
		public Builder element(String value){this.element=value;return this;}
		public Builder speed(float value){this.speed=value;return this;}
		public Builder gravity(double value){this.gravity=value;return this;}
		public Builder knockback(double value){this.knockback=value;return this;}
		public Builder castMode(CastMode value){this.castMode=value;return this;}
		public Builder requirements(List<String> value){this.requirements=value;return this;}
		public Builder execution(Execution value){this.execution=value;return this;}
		public Builder healing(float value){this.healing=value;return this;}
		public Builder range(double value){this.range=value;return this;}
		public Builder maxTargets(int value){this.maxTargets=value;return this;}
		public Builder learningRequirements(List<String> value){this.learningRequirements=value;return this;}
		public Builder timingRank(int value){this.timingRank=value;return this;}
		public Builder officiallyGraded(boolean value){this.officiallyGraded=value;return this;}
		public Builder explicitProfile(CastingProfile value){this.explicitProfile=value;return this;}
		public SpellDefinition build(){return new SpellDefinition(id,manaCost,damage,cooldownTicks,lifetimeTicks,element,speed,gravity,knockback,castMode,requirements,execution,healing,range,maxTargets,learningRequirements,timingRank==0?legacyRank(execution):timingRank,officiallyGraded,explicitProfile);}
	}
}
