package com.example.examplemod.magic.spell.wind.tornado_impact;
import com.example.examplemod.magic.domain.model.SpellId;
import com.example.examplemod.magic.mechanic.projectile.ProjectileSpec;
public final class TornadoImpactSpec {
	public static final ProjectileSpec PROJECTILE=new ProjectileSpec(new SpellId("tornado_impact"),38,20,3F,0F);
	public static final int PREPARATION_TICKS=240,FLIGHT_TICKS=180,MAX_BROKEN_BLOCKS=64;
	public static final double WIDTH=1.5,HEIGHT=1.5,DEPTH=3;
	private TornadoImpactSpec() { }
}
