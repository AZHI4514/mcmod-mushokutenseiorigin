package com.example.examplemod.magic.spell.fire.flame_slice;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
/** Constant penetrating blade, independent of world and presentation. */
public final class FlameSliceSpec {
	public static final ProjectileSpec PROJECTILE = new ProjectileSpec(new SpellId("flame_slice"),4,0,2F,0F);
	public static final float DAMAGE = 4F;
	public static final int FLIGHT_TICKS = 60;
	public static final double RANGE = 120;
	public static final float IGNITION_CHANCE = .8F;
	public static final int IGNITION_TICKS = 100;
	private FlameSliceSpec() { }
}
