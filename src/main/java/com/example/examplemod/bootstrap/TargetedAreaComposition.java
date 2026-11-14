package com.example.examplemod.bootstrap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.server.level.ServerPlayer;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.casting.CastTargetedAreaCommand;
import com.example.examplemod.magic.application.port.TargetedArea;
import com.example.examplemod.infrastructure.fabric.magic.*;
/** Explicit composition of independent effects; no spell interpreter. */
public final class TargetedAreaComposition {
	private static final Map<SpellDefinition,Function<ServerPlayer,TargetedArea>> BINDINGS=Map.ofEntries(
		Map.entry(SpellDefinition.ICICLE_FIELD,IcicleFieldAdapter::new),
		Map.entry(SpellDefinition.WATER_SPLASH,WaterSplashAdapter::new),
		Map.entry(SpellDefinition.ICE_PILLAR,IcePillarAdapter::new),
		Map.entry(SpellDefinition.ICE_FORTRESS,IceFortressAdapter::new));
	public static boolean handles(SpellDefinition spell){return BINDINGS.containsKey(spell);}
	public static java.util.Set<String> ids(){return BINDINGS.keySet().stream().map(SpellDefinition::id).collect(java.util.stream.Collectors.toUnmodifiableSet());}
	public static CastTargetedAreaCommand create(ServerPlayer player,SpellDefinition spell){
		var factory=BINDINGS.get(spell);
		if(factory==null)throw new IllegalArgumentException("No effect binding: "+spell);
		int mana=spell.manaCost();
		int recovery=spell.cooldownTicks();
		var area=factory.apply(player);
		return new CastTargetedAreaCommand(spell.id(),mana,recovery,new PlayerStateAdapter(player),area,List.of(
			()->player.setAttached(MagicAttachments.RECOVERY_TOTAL,recovery),
			()->SequentialMagic.released(player,spell),
			()->MagicGlow.success(player,spell),
			()->MagicSounds.listener.accept(player,MagicSounds.Phase.RELEASE)),error->com.example.examplemod.ExampleMod.LOGGER.error("Committed targeted area observer failed: "+spell.id(),error));
	}
	private TargetedAreaComposition() { }
}
