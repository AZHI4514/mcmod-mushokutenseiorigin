package com.example.examplemod.magic.mechanic.field;
import com.example.examplemod.magic.spell.mixed.nuclear_explosion.NuclearExplosionRules;
/** Immutable field dimensions/upkeep. A spherical volume uses its radius above and below. */
public record MaintainedFieldSpec(int upkeep,boolean followsOwner,double fixedRadius,double height,boolean spherical) {
	public static final MaintainedFieldSpec ATMOSPHERE=new MaintainedFieldSpec(30,true,48,64,false);
	public static final MaintainedFieldSpec GRAVITY=new MaintainedFieldSpec(8,false,6,12,false);
	public static final MaintainedFieldSpec NUCLEAR=new MaintainedFieldSpec(0,false,0,0,true);
	public double radius(int extra){return spherical?NuclearExplosionRules.radius(extra):fixedRadius;}
	public double below(int extra){return spherical?radius(extra):0;}
	public double above(int extra){return spherical?radius(extra):height;}
}
