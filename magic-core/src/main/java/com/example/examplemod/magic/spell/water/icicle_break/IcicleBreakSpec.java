package com.example.examplemod.magic.spell.water.icicle_break;
import com.example.examplemod.magic.domain.combat.EffectRules;
/** Constant-speed piercing shot; historical definition lifetime remains separate from flight budget. */
public final class IcicleBreakSpec {
	public static final com.example.examplemod.magic.mechanic.projectile.ProjectileSpec PROJECTILE=
		new com.example.examplemod.magic.mechanic.projectile.ProjectileSpec(new com.example.examplemod.magic.domain.model.SpellId("icicle_break"),12,10,3F,0F);
	public static final int MANA=12, PREPARATION_TICKS=160, RECOVERY=10, FLIGHT_TICKS=120, LEGACY_LIFETIME=100, FREEZE_TICKS=200;
	public static final float DAMAGE=2F, SPEED=3F, FREEZE_CHANCE=.8F;
	public static final double LEGACY_RANGE=18;
	public static int freezeTicks(float roll,boolean canFreeze,boolean warm) {
		return EffectRules.freezeTicks(roll,FREEZE_CHANCE,FREEZE_TICKS,canFreeze,warm);
	}
	private IcicleBreakSpec() { }
}
