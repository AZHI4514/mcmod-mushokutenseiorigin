package com.example.examplemod.magic.spell.water.ice_blade;
import com.example.examplemod.magic.domain.combat.EffectRules;
/** Eligibility and random samples are supplied by the game adapter after successful damage. */
public final class IceBladeImpactPolicy {
	public static int statusTicks(float roll,boolean canFreeze,boolean warm) {
		return EffectRules.freezeTicks(roll,IceBladeSpec.STATUS_CHANCE,IceBladeSpec.STATUS_TICKS,canFreeze,warm);
	}
	private IceBladeImpactPolicy() { }
}
