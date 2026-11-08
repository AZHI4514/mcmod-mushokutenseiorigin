package com.example.examplemod.magic;

import java.util.List;

/** 服务端参数的单一来源。未接入的施法方式与前置不能被当作可用技能。 */
public record SpellDefinition(String id, int manaCost, float damage, int cooldownTicks,
		int lifetimeTicks, String element, float speed, double gravity, double knockback,
		CastMode castMode, List<String> requirements, Execution execution, float healing,
		double range, int maxTargets, List<String> learningRequirements) {
	public enum Execution { PROJECTILE, FLAME_SLICE, SELF_HEAL, AREA, HEAT_HAND, DETOX, ADVANCED_PROJECTILE, TARGET_AREA, PHASE_TWO }
	public enum CastMode { INSTANT, CHANT, CHARGE, CHANNEL }
	public static final SpellDefinition WATER_BALL = projectile("water_ball", 1, 1, 2, "water", 0.03, 0);
	public static final SpellDefinition FIREBALL = projectile("fireball", 3, 3, 12, "fire", 0.01, 0);
	public static final SpellDefinition WIND_BALL = projectile("wind_ball", 2, 1, 8, "wind", 0.01, 0.6);
	public static final SpellDefinition ROCK_BULLET = projectile("rock_bullet", 3, 4, 12, "earth", 0.05, 0);
	public static final SpellDefinition WATER_ARROW = new SpellDefinition("water_arrow", 2, 2, 6, 40, "water",
			2.5F, 0.03, 0, CastMode.INSTANT, List.of(), Execution.PROJECTILE, 0, 0, 1, List.of("water_ball"));
	public static final SpellDefinition ICE_SMASH = new SpellDefinition("ice_smash", 4, 5, 16, 60, "water",
			1.5F, 0.02, 0, CastMode.INSTANT, List.of(), Execution.PROJECTILE, 0, 0, 1, List.of("water_ball"));
	public static final SpellDefinition FLAME_SLICE = new SpellDefinition("flame_slice", 4, 4, 14, 60, "fire",
			2F, 0, 0, CastMode.INSTANT, List.of(), Execution.FLAME_SLICE, 0, 120, Integer.MAX_VALUE, List.of("fireball"));
	public static final SpellDefinition HEALING = new SpellDefinition("healing", 8, 0, 60, 1, "healing",
			0, 0, 0, CastMode.INSTANT, List.of(), Execution.SELF_HEAL, 4, 0, 1, List.of());
	public static final SpellDefinition WATER_SHIELD = utility("water_shield", 6, 60, 80, "water", Execution.AREA, "water_ball");
	public static final SpellDefinition ICE_BLADE = new SpellDefinition("ice_blade", 5, 5, 20, 21, "water",
			2.5F, 0.02, 0, CastMode.INSTANT, List.of(), Execution.PROJECTILE, 0, 0, 1, List.of("ice_smash"));
	public static final SpellDefinition HEAT_HAND = utility("heat_hand", 2, 10, 200, "fire", Execution.HEAT_HAND, "fireball");
	public static final SpellDefinition HEAT_ISLAND = utility("heat_island", 6, 160, 200, "fire", Execution.AREA, "heat_hand");
	public static final SpellDefinition BURNING_PLACE = new SpellDefinition("burning_place", 10, 2, 60, 80, "fire",
			0, 0, 0, CastMode.INSTANT, List.of(), Execution.AREA, 0, 2, Integer.MAX_VALUE, List.of("fireball"));
	public static final SpellDefinition DETOXIFICATION = utility("detoxification", 6, 60, 1, "healing", Execution.DETOX);
	public static final SpellDefinition MAGIC_SHIELD = utility("magic_shield", 8, 80, 100, "magic", Execution.AREA);
	public static final SpellDefinition WATER_CANNON = intermediate("water_cannon",8,7,30,"water",2F,18,0.8,Execution.ADVANCED_PROJECTILE,"water_arrow");
	public static final SpellDefinition WATER_SPLASH = intermediate("water_splash",8,1,60,"water",0,12,0,Execution.TARGET_AREA,"water_ball");
	public static final SpellDefinition ICICLE_FIELD = intermediate("icicle_field",14,5,120,"water",0,12,0,Execution.TARGET_AREA,"ice_smash","heat_island");
	public static final SpellDefinition EX_FLAME = intermediate("ex_flame",12,8,40,"fire",2F,20,0,Execution.ADVANCED_PROJECTILE,"fireball");
	public static final SpellDefinition SONIC_BOOM = intermediate("sonic_boom",8,5,30,"wind",2F,15,1,Execution.ADVANCED_PROJECTILE,"wind_ball");
	public static final SpellDefinition STONE_CANNON = intermediate("stone_cannon",10,10,40,"earth",3F,24,0,Execution.ADVANCED_PROJECTILE,"rock_bullet");
	private static SpellDefinition intermediate(String id,int mana,float damage,int cooldown,String element,float speed,double range,double knockback,Execution execution,String... prerequisites) {
		return new SpellDefinition(id,mana,damage,cooldown,100,element,speed,0,knockback,
				CastMode.INSTANT,List.of(),execution,0,range,Integer.MAX_VALUE,List.of(prerequisites));
	}
	public static final SpellDefinition ICE_PILLAR = new SpellDefinition("ice_pillar",8,4,60,200,"water",1.5F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,12,Integer.MAX_VALUE,List.of("ice_smash"));
	public static final SpellDefinition WATERFALL = new SpellDefinition("waterfall",4,0,20,1,"water",1.5F,0,0,CastMode.CHANNEL,List.of(),Execution.PHASE_TWO,0,12,Integer.MAX_VALUE,List.of("water_shield"));
	public static final SpellDefinition ICE_LANCE = new SpellDefinition("ice_lance",10,8,40,12,"water",1.5F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,12,Integer.MAX_VALUE,List.of("ice_pillar"));
	public static final SpellDefinition ICICLE_BREAK = new SpellDefinition("icicle_break",12,2,10,100,"water",3F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,18,Integer.MAX_VALUE,List.of("ice_smash"));
	public static final SpellDefinition ICICLE_EDGE = new SpellDefinition("icicle_edge",10,6,30,100,"water",3F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,18,Integer.MAX_VALUE,List.of("ice_blade"));
	public static final SpellDefinition FLOOD_FLUSH = new SpellDefinition("flood_flush",12,2,80,100,"water",0.5F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,8,Integer.MAX_VALUE,List.of("water_shield"));
	public static final SpellDefinition ICE_FORTRESS = new SpellDefinition("ice_fortress",14,0,120,200,"water",1.5F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,12,Integer.MAX_VALUE,List.of("ice_pillar","water_shield"));
	public static final SpellDefinition EXPLOSION = new SpellDefinition("explosion",16,10,80,1,"fire",2F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,12,Integer.MAX_VALUE,List.of("ex_flame"));
	public static final SpellDefinition FLAME_PILLAR = new SpellDefinition("flame_pillar",12,3,80,60,"fire",1.5F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,12,Integer.MAX_VALUE,List.of("burning_place"));
	public static final SpellDefinition FLAMETHROWER = new SpellDefinition("flamethrower",6,4,20,1,"fire",1.5F,0,0,CastMode.CHANNEL,List.of(),Execution.PHASE_TWO,0,16,Integer.MAX_VALUE,List.of("fireball","burning_place"));
	public static final SpellDefinition WIND_SLICE = new SpellDefinition("wind_slice",8,7,20,100,"wind",2F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,20,Integer.MAX_VALUE,List.of("sonic_boom"));
	public static final SpellDefinition WIND_BIND = new SpellDefinition("wind_bind",10,0,100,60,"wind",1.5F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,12,Integer.MAX_VALUE,List.of("wind_ball"));
	public static final SpellDefinition WIND_BLAST = new SpellDefinition("wind_blast",8,0,4,1,"wind",1.5F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,10,Integer.MAX_VALUE,List.of("wind_ball"));
	public static final SpellDefinition EARTH_LANCE = new SpellDefinition("earth_lance",10,6,10,200,"earth",1.5F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,12,Integer.MAX_VALUE,List.of("rock_bullet"));
	public static final SpellDefinition EARTH_PILLAR = new SpellDefinition("earth_pillar",14,8,60,100,"earth",1.5F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,12,Integer.MAX_VALUE,List.of("stone_cannon"));
	public static final SpellDefinition EX_HEALING = new SpellDefinition("ex_healing",16,0,100,1,"healing",1.5F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,8,0,Integer.MAX_VALUE,List.of("healing"));
	public static final SpellDefinition ANTIDOTE_II = new SpellDefinition("antidote_ii",12,0,100,1,"healing",1.5F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,0,Integer.MAX_VALUE,List.of("detoxification"));
	public static final SpellDefinition PHYSICAL_SHIELD = new SpellDefinition("physical_shield",14,0,120,160,"magic",1.5F,0,0,CastMode.INSTANT,List.of(),Execution.PHASE_TWO,0,0,Integer.MAX_VALUE,List.of("magic_shield"));
	public static final List<SpellDefinition> ALL = List.of(WATER_BALL, FIREBALL, WIND_BALL, ROCK_BULLET,
			WATER_ARROW, ICE_SMASH, FLAME_SLICE, HEALING, WATER_SHIELD, ICE_BLADE, HEAT_HAND, HEAT_ISLAND, BURNING_PLACE, DETOXIFICATION, MAGIC_SHIELD, WATER_CANNON, WATER_SPLASH, ICICLE_FIELD, EX_FLAME, SONIC_BOOM, STONE_CANNON, ICE_PILLAR, WATERFALL, ICE_LANCE, ICICLE_BREAK, ICICLE_EDGE, FLOOD_FLUSH, ICE_FORTRESS, EXPLOSION, FLAME_PILLAR, FLAMETHROWER, WIND_SLICE, WIND_BIND, WIND_BLAST, EARTH_LANCE, EARTH_PILLAR, EX_HEALING, ANTIDOTE_II, PHYSICAL_SHIELD);

	private static SpellDefinition utility(String id, int mana, int cooldown, int duration, String element, Execution execution, String... prerequisites) {
		return new SpellDefinition(id, mana, 0, cooldown, duration, element, 0, 0, 0,
				CastMode.INSTANT, List.of(), execution, 0, 0, 1, List.of(prerequisites));
	}

	public enum Motion { BALLISTIC, CONSTANT, FALLING, INSTANT }
	public Motion motion() {
		if (id.equals("earth_pillar")) return Motion.FALLING;
		if (execution == Execution.PROJECTILE) return Motion.BALLISTIC;
		return switch(id) {
			case "flame_slice","water_cannon","ex_flame","sonic_boom","stone_cannon","icicle_break","icicle_edge","flood_flush","wind_slice","explosion" -> Motion.CONSTANT;
			default -> Motion.INSTANT;
		};
	}
	public int flightTicks() { return motion() == Motion.CONSTANT ? MagicRules.flightTicks(rank()) : 0; }
	public double flightRange() { return motion() == Motion.CONSTANT ? speed * flightTicks() : Double.POSITIVE_INFINITY; }
	public boolean piercing() { return motion() == Motion.CONSTANT; }
	public boolean areaDamage() { return switch(id) { case "flame_slice","burning_place","water_cannon","water_splash","icicle_field","ex_flame","ice_pillar","ice_lance","explosion","flamethrower","wind_slice","earth_pillar" -> true; default -> false; }; }
	public int glowColor() {
		if(id.equals("physical_shield")) return 0xBA70FF;
		return switch(element) { case "fire" -> 0xFF5030; case "wind" -> 0xFFFFFF; case "earth" -> 0xA87946; case "healing" -> 0x55FF77; default -> 0x43BFFF; };
	}
	public CastingProfile castingProfile() { return CastingProfile.of(rank(), CastingProfile.Mode.FULL); }
	public int rank() { return execution == Execution.ADVANCED_PROJECTILE || execution == Execution.TARGET_AREA || execution == Execution.PHASE_TWO ? 2 : 1; }
	public int statusTicks() { return id.equals("water_splash") ? 2000 : MagicRules.statusTicks(rank()); }
	public boolean icy() { return id.equals("ice_smash") || id.equals("ice_blade") || id.equals("icicle_field") || id.equals("ice_pillar") || id.equals("ice_lance") || id.equals("icicle_break") || id.equals("icicle_edge") || id.equals("ice_fortress"); }

	/** 原有基础弹体定义保持相同参数与 ID。 */
	public SpellDefinition(String id, int manaCost, float damage, int cooldownTicks, int lifetimeTicks,
			String element, float speed, double gravity, double knockback, CastMode mode, List<String> requirements) {
		this(id, manaCost, damage, cooldownTicks, lifetimeTicks, element, speed, gravity, knockback,
				mode, requirements, Execution.PROJECTILE, 0, 0, 1, List.of());
	}

	public SpellDefinition {
		requirements = List.copyOf(requirements);
		learningRequirements = List.copyOf(learningRequirements);
		// Legacy call-site cooldown arguments are superseded by the shared full-chant profile.
		int spellRank = execution == Execution.ADVANCED_PROJECTILE || execution == Execution.TARGET_AREA || execution == Execution.PHASE_TWO ? 2 : 1;
		cooldownTicks = CastingProfile.of(spellRank, CastingProfile.Mode.FULL).recoveryTicks();
		if (manaCost < 0 || damage < 0 || healing < 0 || range < 0 || maxTargets < 1 || cooldownTicks < 0 || lifetimeTicks < 1)
			throw new IllegalArgumentException("Invalid spell parameters: " + id);
	}

	private static SpellDefinition projectile(String id, int mana, float damage, int cooldown, String element, double gravity, double knockback) {
		return new SpellDefinition(id, mana, damage, cooldown, 100, element, 1.5F, gravity, knockback, CastMode.INSTANT, List.of());
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
		if ((execution != Execution.SELF_HEAL && !id.equals("ex_healing")) || !Float.isFinite(health) || !Float.isFinite(maximum) || health <= 0)
			return 0;
		return Math.min(healing, Math.max(0, maximum - health));
	}

	public static SpellDefinition find(String id) {
		return ALL.stream().filter(spell -> spell.id().equals(id)).findFirst().orElse(null);
	}
}
