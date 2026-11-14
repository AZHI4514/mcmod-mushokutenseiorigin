package com.example.examplemod.magic.spell.water.ice_smash;
import com.example.examplemod.magic.domain.combat.EffectRules;
/** Eligibility and random samples are supplied by the game adapter after successful damage. */
public final class IceSmashImpactPolicy {
	public static int statusTicks(float roll,boolean canFreeze,boolean warm) {
		return EffectRules.freezeTicks(roll,IceSmashSpec.STATUS_CHANCE,IceSmashSpec.STATUS_TICKS,canFreeze,warm);
	}
	private IceSmashImpactPolicy() { }
}
