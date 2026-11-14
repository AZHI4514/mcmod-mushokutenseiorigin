package com.example.examplemod.magic.spell.earth.stone_cannon;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
public final class StoneCannonSpec {
	public static final ProjectileSpec PROJECTILE = new ProjectileSpec(new SpellId("stone_cannon"),10,10,3F,0F);
	public static final int PREPARATION_TICKS=160, FLIGHT_TICKS=120;
	public static final double BODY_SIZE=.25;
	private StoneCannonSpec() { }
}
