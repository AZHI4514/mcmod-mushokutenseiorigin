package com.example.examplemod.bootstrap;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.mechanic.targeting.*;
import com.example.examplemod.magic.spell.holy.exorcist_ray.ExorcistRayPolicy;
public final class HolyInstantComposition {
	public static HolyInstantPolicy policy(SpellDefinition spell){if(spell==SpellDefinition.EXORCIST_JUDGE)return new com.example.examplemod.magic.spell.holy.exorcist_judge.ExorcistJudgePolicy();if(spell==SpellDefinition.EXORCIST_BURST)return new com.example.examplemod.magic.spell.holy.exorcist_burst.ExorcistBurstPolicy();if(spell==SpellDefinition.EXORCIST_RAY)return new ExorcistRayPolicy();if(spell==SpellDefinition.HOLY_RAY)return new com.example.examplemod.magic.spell.holy.holy_ray.HolyRayPolicy();throw new IllegalArgumentException("Not an instant holy spell: "+spell);}
	private HolyInstantComposition() { }
}
