package com.example.examplemod.magic;
import com.example.examplemod.infrastructure.fabric.magic.*;
import com.example.examplemod.infrastructure.fabric.magic.TeleportationAdapter;
import com.example.examplemod.infrastructure.fabric.magic.SummonRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.OceanFreezeRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.CumulonimbusAdapter;
import com.example.examplemod.infrastructure.fabric.magic.SquallAdapter;
import com.example.examplemod.infrastructure.fabric.magic.HolyInstantAdapter;
import com.example.examplemod.infrastructure.fabric.magic.HeatHandAdapter;
import com.example.examplemod.infrastructure.fabric.magic.EarthHedgehogAdapter;
import com.example.examplemod.infrastructure.fabric.magic.CommunicationAdapter;
import com.example.examplemod.infrastructure.fabric.magic.WindFieldRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.SmokeFieldRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.MaintainedFieldRuntimeAdapter;
import com.example.examplemod.infrastructure.fabric.magic.PoweredBarrierRuntimeAdapter;

import com.example.examplemod.infrastructure.fabric.magic.SweptProjectileSpawner;
import java.util.Map;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import com.example.examplemod.infrastructure.fabric.magic.*;
/** Explicit compatibility release composition. Effects here are already paid for by the caller.
 * Preparing commands keep their own transaction boundary; this registry never charges twice.
 */
public final class SpellExecutionBindings {
	@FunctionalInterface private interface Eligibility { boolean test(ServerPlayer player,MagicAim aim); }
	@FunctionalInterface private interface Release { boolean apply(ServerPlayer player,MagicAim castingAim); }
	private record Binding(Eligibility eligibility,Release release) {}
	private static final Map<SpellDefinition,Binding> BINDINGS=Map.ofEntries(
		Map.entry(SpellDefinition.WATER_BALL,new Binding((player,aim)->SpellDefinition.WATER_BALL.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.WATER_BALL.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->launch(player,SpellDefinition.WATER_BALL,castingAim))),
		Map.entry(SpellDefinition.FIREBALL,new Binding((player,aim)->SpellDefinition.FIREBALL.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.FIREBALL.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->launch(player,SpellDefinition.FIREBALL,castingAim))),
		Map.entry(SpellDefinition.WIND_BALL,new Binding((player,aim)->SpellDefinition.WIND_BALL.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.WIND_BALL.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->launch(player,SpellDefinition.WIND_BALL,castingAim))),
		Map.entry(SpellDefinition.ROCK_BULLET,new Binding((player,aim)->SpellDefinition.ROCK_BULLET.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.ROCK_BULLET.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->launch(player,SpellDefinition.ROCK_BULLET,castingAim))),
		Map.entry(SpellDefinition.WATER_ARROW,new Binding((player,aim)->SpellDefinition.WATER_ARROW.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.WATER_ARROW.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->launch(player,SpellDefinition.WATER_ARROW,castingAim))),
		Map.entry(SpellDefinition.ICE_SMASH,new Binding((player,aim)->SpellDefinition.ICE_SMASH.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.ICE_SMASH.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->launch(player,SpellDefinition.ICE_SMASH,castingAim))),
		Map.entry(SpellDefinition.FLAME_SLICE,new Binding((player,aim)->SpellDefinition.FLAME_SLICE.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.FLAME_SLICE.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->launch(player,SpellDefinition.FLAME_SLICE,castingAim))),
		Map.entry(SpellDefinition.HEALING,new Binding((player,aim)->SpellDefinition.HEALING.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.HEALING.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new HealingAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.WATER_SHIELD,new Binding((player,aim)->SpellDefinition.WATER_SHIELD.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.WATER_SHIELD.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new WaterShieldAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.ICE_BLADE,new Binding((player,aim)->SpellDefinition.ICE_BLADE.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.ICE_BLADE.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->launch(player,SpellDefinition.ICE_BLADE,castingAim))),
		Map.entry(SpellDefinition.HEAT_HAND,new Binding((player,aim)->HeatHandAdapter.canUse(player, aim),
			(player,castingAim)->new HeatHandAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.HEAT_ISLAND,new Binding((player,aim)->SpellDefinition.HEAT_ISLAND.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.HEAT_ISLAND.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new HeatIslandAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.BURNING_PLACE,new Binding((player,aim)->SpellDefinition.BURNING_PLACE.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.BURNING_PLACE.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new BurningPlaceAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.DETOXIFICATION,new Binding((player,aim)->player.hasEffect(net.minecraft.world.effect.MobEffects.POISON),
			(player,castingAim)->new DetoxificationAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.MAGIC_SHIELD,new Binding((player,aim)->SpellDefinition.MAGIC_SHIELD.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.MAGIC_SHIELD.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new MagicShieldAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.WATER_CANNON,new Binding((player,aim)->SpellDefinition.WATER_CANNON.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.WATER_CANNON.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->SweptProjectileSpawner.launch(player,SpellDefinition.WATER_CANNON,castingAim,10))),
		Map.entry(SpellDefinition.WATER_SPLASH,new Binding((player,aim)->SpellDefinition.WATER_SPLASH.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.WATER_SPLASH.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new WaterSplashAdapter(player).cast(castingAim))),
		Map.entry(SpellDefinition.ICICLE_FIELD,new Binding((player,aim)->SpellDefinition.ICICLE_FIELD.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.ICICLE_FIELD.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new IcicleFieldAdapter(player).cast(castingAim,10))),
		Map.entry(SpellDefinition.EX_FLAME,new Binding((player,aim)->SpellDefinition.EX_FLAME.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.EX_FLAME.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->SweptProjectileSpawner.launch(player,SpellDefinition.EX_FLAME,castingAim,10))),
		Map.entry(SpellDefinition.SONIC_BOOM,new Binding((player,aim)->SpellDefinition.SONIC_BOOM.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.SONIC_BOOM.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->SweptProjectileSpawner.launch(player,SpellDefinition.SONIC_BOOM,castingAim,10))),
		Map.entry(SpellDefinition.STONE_CANNON,new Binding((player,aim)->SpellDefinition.STONE_CANNON.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.STONE_CANNON.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->SweptProjectileSpawner.launch(player,SpellDefinition.STONE_CANNON,castingAim,10))),
		Map.entry(SpellDefinition.ICE_PILLAR,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->new IcePillarAdapter(player).cast(castingAim))),
		Map.entry(SpellDefinition.WATERFALL,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->EffectCarrierSpawner.launch(player,SpellDefinition.WATERFALL,castingAim))),
		Map.entry(SpellDefinition.ICE_LANCE,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->new IceLanceAdapter(player).cast(castingAim))),
		Map.entry(SpellDefinition.ICICLE_BREAK,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->EffectCarrierSpawner.launch(player,SpellDefinition.ICICLE_BREAK,castingAim))),
		Map.entry(SpellDefinition.ICICLE_EDGE,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->EffectCarrierSpawner.launch(player,SpellDefinition.ICICLE_EDGE,castingAim))),
		Map.entry(SpellDefinition.FLOOD_FLUSH,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->EffectCarrierSpawner.launch(player,SpellDefinition.FLOOD_FLUSH,castingAim))),
		Map.entry(SpellDefinition.ICE_FORTRESS,new Binding((player,aim)->aim.valid()&&(IceFortressAdapter.canCast(player,aim)),
			(player,castingAim)->new IceFortressAdapter(player).cast(castingAim))),
		Map.entry(SpellDefinition.EXPLOSION,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->EffectCarrierSpawner.launch(player,SpellDefinition.EXPLOSION,castingAim))),
		Map.entry(SpellDefinition.FLAME_PILLAR,new Binding((player,aim)->aim.valid()&&(SpellRaycastAdapter.trace(player,aim,SpellDefinition.FLAME_PILLAR.range())!=null),
			(player,castingAim)->new FlamePillarAdapter(player).cast(castingAim))),
		Map.entry(SpellDefinition.FLAMETHROWER,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->EffectCarrierSpawner.launch(player,SpellDefinition.FLAMETHROWER,castingAim))),
		Map.entry(SpellDefinition.WIND_SLICE,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->EffectCarrierSpawner.launch(player,SpellDefinition.WIND_SLICE,castingAim))),
		Map.entry(SpellDefinition.WIND_BIND,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->new WindBindAdapter(player).cast(castingAim))),
		Map.entry(SpellDefinition.WIND_BLAST,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->new WindBlastAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.EARTH_LANCE,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->new EarthLanceAdapter(player).cast(castingAim))),
		Map.entry(SpellDefinition.EARTH_PILLAR,new Binding((player,aim)->aim.valid()&&(SpellRaycastAdapter.aim(player,aim)!=null),
			(player,castingAim)->new EarthPillarAdapter(player).cast(castingAim))),
		Map.entry(SpellDefinition.EX_HEALING,new Binding((player,aim)->aim.valid()&&(SpellDefinition.EX_HEALING.healingAmount(player.getHealth(),player.getMaxHealth())>0),
			(player,castingAim)->new ExHealingAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.ANTIDOTE_II,new Binding((player,aim)->aim.valid()&&(player.hasEffect(net.minecraft.world.effect.MobEffects.POISON)||player.hasEffect(net.minecraft.world.effect.MobEffects.NAUSEA)||player.hasEffect(net.minecraft.world.effect.MobEffects.HUNGER)),
			(player,castingAim)->new AntidoteIIAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.PHYSICAL_SHIELD,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->new PhysicalShieldAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.BLIZZARD_STORM,new Binding((player,aim)->SpellDefinition.BLIZZARD_STORM.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.BLIZZARD_STORM.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new BlizzardStormAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.SQUALL,new Binding((player,aim)->SquallAdapter.target(player,aim)!=null,
			(player,castingAim)->new SquallAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.EXODUS_FLAME,new Binding((player,aim)->SpellDefinition.EXODUS_FLAME.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.EXODUS_FLAME.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->SweptProjectileSpawner.launch(player,SpellDefinition.EXODUS_FLAME,castingAim,10))),
		Map.entry(SpellDefinition.TORNADO_IMPACT,new Binding((player,aim)->SpellDefinition.TORNADO_IMPACT.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.TORNADO_IMPACT.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->SweptProjectileSpawner.launch(player,SpellDefinition.TORNADO_IMPACT,castingAim,10))),
		Map.entry(SpellDefinition.CUMULONIMBUS,new Binding((player,aim)->CumulonimbusAdapter.target(player,aim)!=null,
			(player,castingAim)->new CumulonimbusAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.LIGHTNING,new Binding((player,aim)->LightningAdapter.point(player,aim)!=null,
			(player,castingAim)->false)),
		Map.entry(SpellDefinition.ELECTRIC,new Binding((player,aim)->ElectricAdapter.target(player,aim)!=null,
			(player,castingAim)->new ElectricAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.FROST_NOVA,new Binding((player,aim)->SpellDefinition.FROST_NOVA.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.FROST_NOVA.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new FrostNovaAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.SONIC_BLAST,new Binding((player,aim)->SpellDefinition.SONIC_BLAST.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.SONIC_BLAST.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new SonicBlastAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.EARTH_WALL,new Binding((player,aim)->SpellDefinition.EARTH_WALL.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.EARTH_WALL.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new EarthWallAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.EARTH_FORTRESS,new Binding((player,aim)->SpellDefinition.EARTH_FORTRESS.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.EARTH_FORTRESS.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new EarthFortressAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.EARTH_HEDGEHOG,new Binding((player,aim)->EarthHedgehogAdapter.target(player,aim)!=null,
			(player,castingAim)->new EarthHedgehogAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.QUAGMIRE,new Binding((player,aim)->EarthHedgehogAdapter.target(player,aim)!=null,
			(player,castingAim)->new QuagmireAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.BLAST,new Binding((player,aim)->SpellDefinition.BLAST.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.BLAST.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new BlastAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.BORA_BLAST,new Binding((player,aim)->SpellDefinition.BORA_BLAST.execution() != SpellDefinition.Execution.SELF_HEAL || SpellDefinition.BORA_BLAST.healingAmount(player.getHealth(), player.getMaxHealth()) > 0,
			(player,castingAim)->new BoraBlastAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.MAGMA_GUSH,new Binding((player,aim)->EarthHedgehogAdapter.target(player,aim)!=null,
			(player,castingAim)->new MagmaGushAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.VIOLENT_STORM,new Binding((player,aim)->WindFieldRuntimeAdapter.target(player,SpellDefinition.VIOLENT_STORM,aim)!=null,
			(player,castingAim)->new ViolentStormAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.TORNADO,new Binding((player,aim)->WindFieldRuntimeAdapter.target(player,SpellDefinition.TORNADO,aim)!=null,
			(player,castingAim)->new TornadoAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.VACUUM_STORM,new Binding((player,aim)->WindFieldRuntimeAdapter.target(player,SpellDefinition.VACUUM_STORM,aim)!=null,
			(player,castingAim)->new VacuumStormAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.SKY_TEMPEST,new Binding((player,aim)->WindFieldRuntimeAdapter.target(player,SpellDefinition.SKY_TEMPEST,aim)!=null,
			(player,castingAim)->new SkyTempestAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.FLASH_OVER,new Binding((player,aim)->WindFieldRuntimeAdapter.target(player,SpellDefinition.FLASH_OVER,aim)!=null,
			(player,castingAim)->new FlashOverAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.FLAME_TORNADO,new Binding((player,aim)->WindFieldRuntimeAdapter.target(player,SpellDefinition.FLAME_TORNADO,aim)!=null,
			(player,castingAim)->new FlameTornadoAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.INFERNO,new Binding((player,aim)->WindFieldRuntimeAdapter.target(player,SpellDefinition.INFERNO,aim)!=null,
			(player,castingAim)->new InfernoAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.ABSOLUTE_ZERO,new Binding((player,aim)->WindFieldRuntimeAdapter.target(player,SpellDefinition.ABSOLUTE_ZERO,aim)!=null,
			(player,castingAim)->new AbsoluteZeroAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.EXORCIST_RAY,new Binding((player,aim)->HolyInstantAdapter.canCast(player,SpellDefinition.EXORCIST_RAY,aim),
			(player,castingAim)->new ExorcistRayAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.EXORCIST_BURST,new Binding((player,aim)->HolyInstantAdapter.canCast(player,SpellDefinition.EXORCIST_BURST,aim),
			(player,castingAim)->new ExorcistBurstAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.EXORCIST_JUDGE,new Binding((player,aim)->HolyInstantAdapter.canCast(player,SpellDefinition.EXORCIST_JUDGE,aim),
			(player,castingAim)->new ExorcistJudgeAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.HOLY_RAY,new Binding((player,aim)->HolyInstantAdapter.canCast(player,SpellDefinition.HOLY_RAY,aim),
			(player,castingAim)->new HolyRayAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.SACRED_NOVA,new Binding((player,aim)->HolyInstantAdapter.canCast(player,SpellDefinition.SACRED_NOVA,aim),
			(player,castingAim)->new SacredNovaAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.DIVINE_JUDGE,new Binding((player,aim)->HolyInstantAdapter.canCast(player,SpellDefinition.DIVINE_JUDGE,aim),
			(player,castingAim)->new DivineJudgeAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.GOD_PURGE,new Binding((player,aim)->HolyInstantAdapter.canCast(player,SpellDefinition.GOD_PURGE,aim),
			(player,castingAim)->new GodPurgeAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.FLAME_STORM,new Binding((player,aim)->com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.target(player,SpellDefinition.FLAME_STORM,aim)!=null,
			(player,castingAim)->new FlameStormAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.APOCALYPSE_FLAME,new Binding((player,aim)->com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.target(player,SpellDefinition.APOCALYPSE_FLAME,aim)!=null,
			(player,castingAim)->new ApocalypseFlameAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.EARTHQUAKE,new Binding((player,aim)->com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.target(player,SpellDefinition.EARTHQUAKE,aim)!=null,
			(player,castingAim)->new EarthquakeAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.TERRA_FALL,new Binding((player,aim)->com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.target(player,SpellDefinition.TERRA_FALL,aim)!=null,
			(player,castingAim)->new TerraFallAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.CONTINENTAL_SINK,new Binding((player,aim)->com.example.examplemod.infrastructure.fabric.magic.GroundFieldRuntimeAdapter.target(player,SpellDefinition.CONTINENTAL_SINK,aim)!=null,
			(player,castingAim)->new ContinentalSinkAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.SAND_CLOUD,new Binding((player,aim)->SmokeFieldRuntimeAdapter.canCast(player,SpellDefinition.SAND_CLOUD,aim),
			(player,castingAim)->new SandCloudAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.DUST_STORM,new Binding((player,aim)->SmokeFieldRuntimeAdapter.canCast(player,SpellDefinition.DUST_STORM,aim),
			(player,castingAim)->new DustStormAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.SANDSTORM,new Binding((player,aim)->SmokeFieldRuntimeAdapter.canCast(player,SpellDefinition.SANDSTORM,aim),
			(player,castingAim)->new SandstormAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.DEEP_MIST,new Binding((player,aim)->SmokeFieldRuntimeAdapter.canCast(player,SpellDefinition.DEEP_MIST,aim),
			(player,castingAim)->new DeepMistAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.MUD_RAIN,new Binding((player,aim)->SmokeFieldRuntimeAdapter.canCast(player,SpellDefinition.MUD_RAIN,aim),
			(player,castingAim)->new MudRainAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.ANTIDOTE_III,new Binding((player,aim)->AntidoteIIIAdapter.canCure(player,aim),
			(player,castingAim)->new AntidoteIIIAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.HOWLING,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->new HowlingAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.HYPNOSIS_MAGIC,new Binding((player,aim)->HypnosisAdapter.canCast(player,aim),
			(player,castingAim)->new HypnosisAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.ATMOSPHERE_DOMINION,new Binding((player,aim)->MaintainedFieldRuntimeAdapter.canCast(player,SpellDefinition.ATMOSPHERE_DOMINION,aim),
			(player,castingAim)->new AtmosphereDominionAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.GRAVITY_MAGIC,new Binding((player,aim)->MaintainedFieldRuntimeAdapter.canCast(player,SpellDefinition.GRAVITY_MAGIC,aim),
			(player,castingAim)->new GravityMagicAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.NUCLEAR_EXPLOSION,new Binding((player,aim)->MaintainedFieldRuntimeAdapter.canCast(player,SpellDefinition.NUCLEAR_EXPLOSION,aim),
			(player,castingAim)->new NuclearExplosionAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.DUAL_SHIELD,new Binding((player,aim)->com.example.examplemod.infrastructure.fabric.magic.FiniteBarrierRuntimeAdapter.target(player,SpellDefinition.DUAL_SHIELD,aim)!=null,
			(player,castingAim)->new DualShieldAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.SPIRIT_SEALING_BARRIER,new Binding((player,aim)->com.example.examplemod.infrastructure.fabric.magic.FiniteBarrierRuntimeAdapter.target(player,SpellDefinition.SPIRIT_SEALING_BARRIER,aim)!=null,
			(player,castingAim)->new SpiritSealingBarrierAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.CONFINEMENT_BARRIER,new Binding((player,aim)->com.example.examplemod.infrastructure.fabric.magic.FiniteBarrierRuntimeAdapter.target(player,SpellDefinition.CONFINEMENT_BARRIER,aim)!=null,
			(player,castingAim)->new ConfinementBarrierAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.DISTURB_MAGIC,new Binding((player,aim)->aim.valid(),
			(player,castingAim)->new DisturbMagicAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.DIVINATION_MAGIC,new Binding((player,aim)->DivinationMagicAdapter.canCast(player,aim),
			(player,castingAim)->new DivinationMagicAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.MONSTER_SUMMON,new Binding((player,aim)->SummonRuntimeAdapter.canCast(player,SpellDefinition.MONSTER_SUMMON,aim),
			(player,castingAim)->new MonsterSummonAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.SPIRIT_SUMMON,new Binding((player,aim)->SummonRuntimeAdapter.canCast(player,SpellDefinition.SPIRIT_SUMMON,aim),
			(player,castingAim)->new SpiritSummonAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.LAMPLIGHT_SPIRITS,new Binding((player,aim)->SummonRuntimeAdapter.canCast(player,SpellDefinition.LAMPLIGHT_SPIRITS,aim),
			(player,castingAim)->new LamplightSpiritsAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.NECROMANCY,new Binding((player,aim)->SummonRuntimeAdapter.canCast(player,SpellDefinition.NECROMANCY,aim),
			(player,castingAim)->new NecromancyAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.FORTRESS_BARRIER,new Binding((player,aim)->PoweredBarrierRuntimeAdapter.canCast(player,SpellDefinition.FORTRESS_BARRIER,aim),
			(player,castingAim)->new FortressBarrierAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.DEMON_GOD_SEALING_BARRIER,new Binding((player,aim)->PoweredBarrierRuntimeAdapter.canCast(player,SpellDefinition.DEMON_GOD_SEALING_BARRIER,aim),
			(player,castingAim)->new DemonGodSealingBarrierAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.SAINT_DRAGON_BATTLE_AURA,new Binding((player,aim)->PoweredBarrierRuntimeAdapter.canCast(player,SpellDefinition.SAINT_DRAGON_BATTLE_AURA,aim),
			(player,castingAim)->new DragonAuraAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.COMMUNICATION_MAGIC,new Binding((player,aim)->CommunicationAdapter.canCast(player),
			(player,castingAim)->new CommunicationAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.TELEPORTATION_MAGIC,new Binding((player,aim)->TeleportationAdapter.canCast(player),
			(player,castingAim)->new TeleportationAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.ILLUSION_BARRIER,new Binding((player,aim)->WindFieldRuntimeAdapter.target(player,SpellDefinition.ILLUSION_BARRIER,aim)!=null,
			(player,castingAim)->new IllusionAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))),
		Map.entry(SpellDefinition.OCEAN_FREEZE,new Binding((player,aim)->OceanFreezeRuntimeAdapter.target(player,aim)!=null,
			(player,castingAim)->new OceanFreezeAdapter(player).apply(castingAim.yaw(),castingAim.pitch()))));
	static {
		if(!BINDINGS.keySet().equals(java.util.Set.copyOf(SpellDefinition.ALL)))
			throw new ExceptionInInitializerError("Spell execution bindings must exactly cover the catalog");
	}
	public static void validate() { /* Class initialization verifies duplicate keys and complete coverage. */ }
	public static boolean canExecute(ServerPlayer player,SpellDefinition spell,MagicAim aim) {
		var binding=BINDINGS.get(spell);return binding!=null&&binding.eligibility().test(player,aim);
	}
	public static boolean execute(ServerPlayer player,SpellDefinition spell,MagicAim aim) {
		var binding=BINDINGS.get(spell);return binding!=null&&binding.release().apply(player,aim);
	}
	private static boolean launch(ServerPlayer player,SpellDefinition spell,MagicAim castingAim) {
		Projectile projectile = spell.execution() == SpellDefinition.Execution.FLAME_SLICE
				? new FlameSliceEntity(MagicSystem.FLAME_SLICE, player.level())
				: new WaterBallEntity(MagicSystem.projectileType(spell), player.level());
		projectile.setOwner(player);
		projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
		if (spell.execution() == SpellDefinition.Execution.FLAME_SLICE || spell == SpellDefinition.ICE_BLADE) {
			// 从视线原点沿请求中的准星方向发射，避免服务端旧朝向及垂直偏移。
			projectile.setPos(player.getEyePosition());
			var aim = castingAim.direction();
			projectile.shoot(aim.x, aim.y, aim.z, spell.speed(), 0);
		} else projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, spell.speed(), 1.0F);
		return player.level().addFreshEntity(projectile);
	}

	private SpellExecutionBindings() {}
}
