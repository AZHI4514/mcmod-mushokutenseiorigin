package com.example.examplemod.bootstrap;
import com.example.examplemod.magic.SpellDefinition;
import com.example.examplemod.magic.spell.wind.atmosphere_dominion.AtmosphereDominionRules;
import com.example.examplemod.magic.spell.mixed.nuclear_explosion.NuclearExplosionRules;
public final class SpellModeComposition {
	public static int count(SpellDefinition spell){return spell==SpellDefinition.ATMOSPHERE_DOMINION?AtmosphereDominionRules.MODE_COUNT:spell==SpellDefinition.NUCLEAR_EXPLOSION?NuclearExplosionRules.MODE_COUNT:2;}
	private SpellModeComposition() { }
}
