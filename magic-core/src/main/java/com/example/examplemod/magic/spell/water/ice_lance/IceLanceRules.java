package com.example.examplemod.magic.spell.water.ice_lance;
import com.example.examplemod.magic.domain.combat.EffectRules;
/** Age is the existing entity age after increment; later ticks never repeat the initial hit. */
public final class IceLanceRules {
	public static boolean damageDue(int age) { return age==1; }
	public static int freezeTicks(float roll,boolean canFreeze,boolean warm) {
		return EffectRules.freezeTicks(roll,IceLanceSpec.FREEZE_CHANCE,IceLanceSpec.FREEZE_TICKS,canFreeze,warm);
	}
	private IceLanceRules() { }
}
