package com.example.examplemod.bootstrap;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.field.*;
import com.example.examplemod.magic.spell.wind.violent_storm.ViolentStormPolicy;
public final class WindFieldComposition {
	public static WindFieldPolicy policy(SpellDefinition spell){
		if(spell==SpellDefinition.SKY_TEMPEST)return new com.example.examplemod.magic.spell.wind.sky_tempest.SkyTempestPolicy();
		if(spell==SpellDefinition.VACUUM_STORM)return new com.example.examplemod.magic.spell.wind.vacuum_storm.VacuumStormPolicy();
		if(spell==SpellDefinition.TORNADO)return new com.example.examplemod.magic.spell.wind.tornado.TornadoPolicy();
		if(spell==SpellDefinition.VIOLENT_STORM)return new ViolentStormPolicy();
		if(spell==SpellDefinition.FLAME_TORNADO)return new com.example.examplemod.magic.spell.mixed.flame_tornado.FlameTornadoPolicy();
		throw new IllegalArgumentException("No wind field policy: "+spell);
	}
	private WindFieldComposition() { }
}
