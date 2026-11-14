package com.example.examplemod.magic.spell.water.icicle_edge;
import com.example.examplemod.magic.domain.combat.EffectRules;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
/** Piercing cross-section shot; do not replace full-section obstruction with an ordinary ray. */
public final class IcicleEdgeSpec {
	public static final int MANA=10, RECOVERY=10, PREPARATION_TICKS=160, FLIGHT_TICKS=120, LEGACY_LIFETIME=100, FREEZE_TICKS=200;
	public static final float DAMAGE=6F, SPEED=3F;
	public static final double LEGACY_RANGE=18, WIDTH=3, HEIGHT=1, DEPTH=1;
	public static final ProjectileSpec PROJECTILE=new ProjectileSpec(new SpellId("icicle_edge"),MANA,RECOVERY,SPEED,0F);
	public static int freezeTicks(float roll,boolean canFreeze,boolean warm) { return EffectRules.freezeTicks(roll,.8F,FREEZE_TICKS,canFreeze,warm); }
	private IcicleEdgeSpec() { }
}
