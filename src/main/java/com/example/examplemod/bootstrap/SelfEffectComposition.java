package com.example.examplemod.bootstrap;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.casting.CastSelfEffectCommand;
import com.example.examplemod.infrastructure.fabric.magic.*;
/** Explicit wiring, not a Factory Method or generic effect interpreter. */
public final class SelfEffectComposition {
	private static final java.util.Map<SpellDefinition,java.util.function.Function<ServerPlayer,com.example.examplemod.magic.application.port.SelfEffect>> BINDINGS=java.util.Map.ofEntries(
		java.util.Map.entry(SpellDefinition.SACRED_NOVA,SacredNovaAdapter::new),
		java.util.Map.entry(SpellDefinition.HOLY_RAY,HolyRayAdapter::new),
		java.util.Map.entry(SpellDefinition.EXORCIST_JUDGE,ExorcistJudgeAdapter::new),
		java.util.Map.entry(SpellDefinition.EXORCIST_BURST,ExorcistBurstAdapter::new),
		java.util.Map.entry(SpellDefinition.EXORCIST_RAY,ExorcistRayAdapter::new),
		java.util.Map.entry(SpellDefinition.ABSOLUTE_ZERO,AbsoluteZeroAdapter::new),
		java.util.Map.entry(SpellDefinition.INFERNO,InfernoAdapter::new),
		java.util.Map.entry(SpellDefinition.FLAME_TORNADO,FlameTornadoAdapter::new),
		java.util.Map.entry(SpellDefinition.FLASH_OVER,FlashOverAdapter::new),
		java.util.Map.entry(SpellDefinition.SKY_TEMPEST,SkyTempestAdapter::new),
		java.util.Map.entry(SpellDefinition.VACUUM_STORM,VacuumStormAdapter::new),
		java.util.Map.entry(SpellDefinition.TORNADO,TornadoAdapter::new),
		java.util.Map.entry(SpellDefinition.VIOLENT_STORM,ViolentStormAdapter::new),
		java.util.Map.entry(SpellDefinition.MAGMA_GUSH,MagmaGushAdapter::new),
		java.util.Map.entry(SpellDefinition.QUAGMIRE,QuagmireAdapter::new),
		java.util.Map.entry(SpellDefinition.EARTH_HEDGEHOG,EarthHedgehogAdapter::new),
		java.util.Map.entry(SpellDefinition.EARTH_FORTRESS,EarthFortressAdapter::new),
		java.util.Map.entry(SpellDefinition.EARTH_WALL,EarthWallAdapter::new),
		java.util.Map.entry(SpellDefinition.ELECTRIC,ElectricAdapter::new),
		java.util.Map.entry(SpellDefinition.CUMULONIMBUS,CumulonimbusAdapter::new),
		java.util.Map.entry(SpellDefinition.SQUALL,SquallAdapter::new),
		java.util.Map.entry(SpellDefinition.BLIZZARD_STORM,BlizzardStormAdapter::new),
		java.util.Map.entry(SpellDefinition.MAGIC_SHIELD,MagicShieldAdapter::new),
		java.util.Map.entry(SpellDefinition.BURNING_PLACE,BurningPlaceAdapter::new),
		java.util.Map.entry(SpellDefinition.HEAT_ISLAND,HeatIslandAdapter::new),
		java.util.Map.entry(SpellDefinition.HEAT_HAND,HeatHandAdapter::new),
		java.util.Map.entry(SpellDefinition.WATER_SHIELD,WaterShieldAdapter::new),
		java.util.Map.entry(SpellDefinition.BORA_BLAST,BoraBlastAdapter::new),
		java.util.Map.entry(SpellDefinition.BLAST,BlastAdapter::new),
		java.util.Map.entry(SpellDefinition.SONIC_BLAST,SonicBlastAdapter::new),
		java.util.Map.entry(SpellDefinition.FROST_NOVA,FrostNovaAdapter::new),
		java.util.Map.entry(SpellDefinition.PHYSICAL_SHIELD,PhysicalShieldAdapter::new),
		java.util.Map.entry(SpellDefinition.EX_HEALING,ExHealingAdapter::new),
		java.util.Map.entry(SpellDefinition.WIND_BLAST,WindBlastAdapter::new),
		java.util.Map.entry(SpellDefinition.GOD_PURGE,GodPurgeAdapter::new),
		java.util.Map.entry(SpellDefinition.DIVINE_JUDGE,DivineJudgeAdapter::new),
		java.util.Map.entry(SpellDefinition.FLAME_STORM,FlameStormAdapter::new),
		java.util.Map.entry(SpellDefinition.APOCALYPSE_FLAME,ApocalypseFlameAdapter::new),
		java.util.Map.entry(SpellDefinition.EARTHQUAKE,EarthquakeAdapter::new),
		java.util.Map.entry(SpellDefinition.TERRA_FALL,TerraFallAdapter::new),
		java.util.Map.entry(SpellDefinition.CONTINENTAL_SINK,ContinentalSinkAdapter::new),
		java.util.Map.entry(SpellDefinition.SAND_CLOUD,SandCloudAdapter::new),
		java.util.Map.entry(SpellDefinition.DUST_STORM,DustStormAdapter::new),
		java.util.Map.entry(SpellDefinition.SANDSTORM,SandstormAdapter::new),
		java.util.Map.entry(SpellDefinition.DEEP_MIST,DeepMistAdapter::new),
		java.util.Map.entry(SpellDefinition.MUD_RAIN,MudRainAdapter::new),
		java.util.Map.entry(SpellDefinition.OCEAN_FREEZE,OceanFreezeAdapter::new),
		java.util.Map.entry(SpellDefinition.ATMOSPHERE_DOMINION,AtmosphereDominionAdapter::new),
		java.util.Map.entry(SpellDefinition.GRAVITY_MAGIC,GravityMagicAdapter::new),
		java.util.Map.entry(SpellDefinition.NUCLEAR_EXPLOSION,NuclearExplosionAdapter::new),
		java.util.Map.entry(SpellDefinition.DUAL_SHIELD,DualShieldAdapter::new),
		java.util.Map.entry(SpellDefinition.SPIRIT_SEALING_BARRIER,SpiritSealingBarrierAdapter::new),
		java.util.Map.entry(SpellDefinition.CONFINEMENT_BARRIER,ConfinementBarrierAdapter::new),
		java.util.Map.entry(SpellDefinition.SAINT_DRAGON_BATTLE_AURA,DragonAuraAdapter::new),
		java.util.Map.entry(SpellDefinition.FORTRESS_BARRIER,FortressBarrierAdapter::new),
		java.util.Map.entry(SpellDefinition.DEMON_GOD_SEALING_BARRIER,DemonGodSealingBarrierAdapter::new),
		java.util.Map.entry(SpellDefinition.HEALING,HealingAdapter::new),
		java.util.Map.entry(SpellDefinition.DETOXIFICATION,DetoxificationAdapter::new),
		java.util.Map.entry(SpellDefinition.ANTIDOTE_II,AntidoteIIAdapter::new),
		java.util.Map.entry(SpellDefinition.ANTIDOTE_III,AntidoteIIIAdapter::new),
		java.util.Map.entry(SpellDefinition.HOWLING,HowlingAdapter::new),
		java.util.Map.entry(SpellDefinition.HYPNOSIS_MAGIC,HypnosisAdapter::new),
		java.util.Map.entry(SpellDefinition.DISTURB_MAGIC,DisturbMagicAdapter::new),
		java.util.Map.entry(SpellDefinition.DIVINATION_MAGIC,DivinationMagicAdapter::new),
		java.util.Map.entry(SpellDefinition.ILLUSION_BARRIER,IllusionAdapter::new),
		java.util.Map.entry(SpellDefinition.MONSTER_SUMMON,MonsterSummonAdapter::new),
		java.util.Map.entry(SpellDefinition.SPIRIT_SUMMON,SpiritSummonAdapter::new),
		java.util.Map.entry(SpellDefinition.LAMPLIGHT_SPIRITS,LamplightSpiritsAdapter::new),
		java.util.Map.entry(SpellDefinition.NECROMANCY,NecromancyAdapter::new),
		java.util.Map.entry(SpellDefinition.COMMUNICATION_MAGIC,CommunicationAdapter::new),
		java.util.Map.entry(SpellDefinition.TELEPORTATION_MAGIC,TeleportationAdapter::new));
	public static boolean handles(SpellDefinition spell){return BINDINGS.containsKey(spell);}
	public static java.util.Set<String> ids(){return BINDINGS.keySet().stream().map(SpellDefinition::id).collect(java.util.stream.Collectors.toUnmodifiableSet());}
	public static CastSelfEffectCommand create(ServerPlayer player,SpellDefinition spell) {
		if(!handles(spell))throw new IllegalArgumentException("No self effect binding: "+spell);
		int mana=spell.manaCost();
		int recovery=spell.cooldownTicks();
		var effect=BINDINGS.get(spell).apply(player);
		return new CastSelfEffectCommand(spell.id(),mana,recovery,new PlayerStateAdapter(player),effect,List.of(
			()->player.setAttached(MagicAttachments.RECOVERY_TOTAL,recovery),
			()->SequentialMagic.released(player,spell),
			()->MagicGlow.success(player,spell),
			()->MagicSounds.listener.accept(player,MagicSounds.Phase.RELEASE)),error->com.example.examplemod.ExampleMod.LOGGER.error("Committed self effect observer failed: "+spell.id(),error));
	}
	private SelfEffectComposition() { }
}
