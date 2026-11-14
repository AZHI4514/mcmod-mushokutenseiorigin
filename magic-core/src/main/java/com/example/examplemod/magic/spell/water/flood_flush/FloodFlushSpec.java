package com.example.examplemod.magic.spell.water.flood_flush;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
public final class FloodFlushSpec {
	public static final int MANA=12, RECOVERY=10, PREPARATION_TICKS=160, FLIGHT_TICKS=120, LEGACY_LIFETIME=100, WET_TICKS=200;
	public static final float DAMAGE=2F, SPEED=.5F;
	public static final double LEGACY_RANGE=8, KNOCKBACK=1.2, WIDTH=3, HEIGHT=1, DEPTH=1;
	public static final ProjectileSpec PROJECTILE=new ProjectileSpec(new SpellId("flood_flush"),MANA,RECOVERY,SPEED,0F);
	public static int wetTicks(float roll) { return roll<.6F?WET_TICKS:0; }
	private FloodFlushSpec() { }
}
