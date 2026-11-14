package com.example.examplemod.bootstrap;
import com.example.examplemod.magic.*;
import com.example.examplemod.ExampleMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
/** Sole entity registration owner. Compatibility aliases remain in MagicSystem. */
public final class MagicEntityTypes {
	public static final EntityType<WaterBallEntity> WATER_BALL = registerProjectile("water_ball");
	public static final EntityType<WaterBallEntity> FIREBALL = registerProjectile("fireball");
	public static final EntityType<WaterBallEntity> WIND_BALL = registerProjectile("wind_ball");
	public static final EntityType<WaterBallEntity> ROCK_BULLET = registerProjectile("rock_bullet");
	public static final EntityType<WaterBallEntity> WATER_ARROW = registerProjectile("water_arrow");
	public static final EntityType<WaterBallEntity> ICE_SMASH = registerProjectile("ice_smash");
	public static final EntityType<WaterBallEntity> ICE_BLADE = registerProjectile("ice_blade");
	public static final EntityType<MagicAreaEntity> MAGIC_AREA = Registry.register(BuiltInRegistries.ENTITY_TYPE,
			ExampleMod.id("magic_area"), EntityType.Builder.<MagicAreaEntity>of(MagicAreaEntity::new, MobCategory.MISC)
					.sized(0.1F, 0.1F).clientTrackingRange(6).updateInterval(1)
					.build(ResourceKey.create(Registries.ENTITY_TYPE, ExampleMod.id("magic_area"))));
	public static final EntityType<FlameSliceEntity> FLAME_SLICE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
			ExampleMod.id("flame_slice"), EntityType.Builder.<FlameSliceEntity>of(FlameSliceEntity::new, MobCategory.MISC)
					.sized(1.5F, 0.25F).clientTrackingRange(4).updateInterval(1)
					.build(ResourceKey.create(Registries.ENTITY_TYPE, ExampleMod.id("flame_slice"))));

	public static final EntityType<SweptSpellProjectile> WATER_CANNON=registerIntermediate("water_cannon",0.25F);
	public static final EntityType<SweptSpellProjectile> WATER_SPLASH=registerIntermediate("water_splash",0.1F);
	public static final EntityType<SweptSpellProjectile> ICICLE_FIELD=registerIntermediate("icicle_field",0.1F);
	public static final EntityType<SweptSpellProjectile> TORNADO_IMPACT=registerIntermediate("tornado_impact",1.5F);
	public static final EntityType<SweptSpellProjectile> EXODUS_FLAME=registerIntermediate("exodus_flame",3F);
	public static final EntityType<SweptSpellProjectile> EX_FLAME=registerIntermediate("ex_flame",2F);
	public static final EntityType<SweptSpellProjectile> SONIC_BOOM=registerIntermediate("sonic_boom",1F);
	public static final EntityType<SweptSpellProjectile> STONE_CANNON=registerIntermediate("stone_cannon",0.25F);
	public static final EntityType<DirectionalSpellCarrier> PHASE_TWO=Registry.register(BuiltInRegistries.ENTITY_TYPE,ExampleMod.id("phase_two_spell"),EntityType.Builder.<DirectionalSpellCarrier>of(DirectionalSpellCarrier::new,MobCategory.MISC).sized(0.25F,0.25F).clientTrackingRange(8).updateInterval(1).build(ResourceKey.create(Registries.ENTITY_TYPE,ExampleMod.id("phase_two_spell"))));
	private static final java.util.Map<EntityType<?>,SpellDefinition> BASIC_SPELLS=java.util.Map.ofEntries(
		java.util.Map.entry(WATER_BALL,SpellDefinition.WATER_BALL),
		java.util.Map.entry(FIREBALL,SpellDefinition.FIREBALL),
		java.util.Map.entry(WIND_BALL,SpellDefinition.WIND_BALL),
		java.util.Map.entry(ROCK_BULLET,SpellDefinition.ROCK_BULLET),
		java.util.Map.entry(WATER_ARROW,SpellDefinition.WATER_ARROW),
		java.util.Map.entry(ICE_SMASH,SpellDefinition.ICE_SMASH),
		java.util.Map.entry(ICE_BLADE,SpellDefinition.ICE_BLADE));
	public static SpellDefinition basicSpell(EntityType<?> type){return BASIC_SPELLS.getOrDefault(type,SpellDefinition.WATER_BALL);}
	private static final java.util.Map<EntityType<?>,SpellDefinition> SWEPT_SPELLS=java.util.Map.ofEntries(
		java.util.Map.entry(WATER_CANNON,SpellDefinition.WATER_CANNON),
		java.util.Map.entry(WATER_SPLASH,SpellDefinition.WATER_SPLASH),
		java.util.Map.entry(ICICLE_FIELD,SpellDefinition.ICICLE_FIELD),
		java.util.Map.entry(TORNADO_IMPACT,SpellDefinition.TORNADO_IMPACT),
		java.util.Map.entry(EXODUS_FLAME,SpellDefinition.EXODUS_FLAME),
		java.util.Map.entry(EX_FLAME,SpellDefinition.EX_FLAME),
		java.util.Map.entry(SONIC_BOOM,SpellDefinition.SONIC_BOOM),
		java.util.Map.entry(STONE_CANNON,SpellDefinition.STONE_CANNON));
	public static SpellDefinition sweptSpell(EntityType<?> type){return SWEPT_SPELLS.getOrDefault(type,SpellDefinition.WATER_CANNON);}
	private static EntityType<SweptSpellProjectile> registerIntermediate(String id,float size) {
		return Registry.register(BuiltInRegistries.ENTITY_TYPE,ExampleMod.id(id),EntityType.Builder.<SweptSpellProjectile>of(SweptSpellProjectile::new,MobCategory.MISC)
				.sized(size,size).clientTrackingRange(8).updateInterval(1).build(ResourceKey.create(Registries.ENTITY_TYPE,ExampleMod.id(id))));
	}
	public static EntityType<SweptSpellProjectile> intermediateType(SpellDefinition spell) {
		return switch(spell.id()) {
			case "water_cannon"->WATER_CANNON; case "water_splash"->WATER_SPLASH; case "icicle_field"->ICICLE_FIELD;
			case "tornado_impact"->TORNADO_IMPACT; case "exodus_flame"->EXODUS_FLAME; case "ex_flame"->EX_FLAME; case "sonic_boom"->SONIC_BOOM; case "stone_cannon"->STONE_CANNON;
			default->throw new IllegalArgumentException("Unknown intermediate spell");
		};
	}
	private static EntityType<WaterBallEntity> registerProjectile(String id) {
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, ExampleMod.id(id),
				EntityType.Builder.<WaterBallEntity>of(WaterBallEntity::new, MobCategory.MISC)
						.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(1)
						.build(ResourceKey.create(Registries.ENTITY_TYPE, ExampleMod.id(id))));
	}

	public static EntityType<WaterBallEntity> projectileType(SpellDefinition spell) {
		return switch (spell.id()) {
			case "water_ball" -> WATER_BALL;
			case "fireball" -> FIREBALL;
			case "wind_ball" -> WIND_BALL;
			case "rock_bullet" -> ROCK_BULLET;
			case "water_arrow" -> WATER_ARROW;
			case "ice_smash" -> ICE_SMASH;
			case "ice_blade" -> ICE_BLADE;
			default -> throw new IllegalArgumentException("No spell executor: " + spell.id());
		};
	}

	private MagicEntityTypes() {}
}
