package com.example.examplemod.bootstrap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.server.level.ServerPlayer;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.casting.CastLocalEffectCommand;
import com.example.examplemod.magic.application.port.LocalEffect;
import com.example.examplemod.infrastructure.fabric.magic.*;
/** Explicit composition of independent effects; no spell interpreter. */
public final class LocalEffectComposition {
	private static final Map<SpellDefinition,Function<ServerPlayer,LocalEffect>> BINDINGS=Map.ofEntries(
		Map.entry(SpellDefinition.ICE_LANCE,IceLanceAdapter::new),
		Map.entry(SpellDefinition.FLAME_PILLAR,FlamePillarAdapter::new),
		Map.entry(SpellDefinition.WIND_BIND,WindBindAdapter::new),
		Map.entry(SpellDefinition.EARTH_LANCE,EarthLanceAdapter::new),
		Map.entry(SpellDefinition.EARTH_PILLAR,EarthPillarAdapter::new));
	public static boolean handles(SpellDefinition spell){return BINDINGS.containsKey(spell);}
	public static java.util.Set<String> ids(){return BINDINGS.keySet().stream().map(SpellDefinition::id).collect(java.util.stream.Collectors.toUnmodifiableSet());}
	public static CastLocalEffectCommand create(ServerPlayer player,SpellDefinition spell){
		var factory=BINDINGS.get(spell);
		if(factory==null)throw new IllegalArgumentException("No effect binding: "+spell);
		int mana=spell.manaCost();
		int recovery=spell.cooldownTicks();
		var effect=factory.apply(player);
		return new CastLocalEffectCommand(spell.id(),mana,recovery,new PlayerStateAdapter(player),effect,List.of(
			()->player.setAttached(MagicAttachments.RECOVERY_TOTAL,recovery),
			()->SequentialMagic.released(player,spell),
			()->MagicGlow.success(player,spell),
			()->MagicSounds.listener.accept(player,MagicSounds.Phase.RELEASE)),error->com.example.examplemod.ExampleMod.LOGGER.error("Committed local effect observer failed: "+spell.id(),error));
	}
	private LocalEffectComposition() { }
}
