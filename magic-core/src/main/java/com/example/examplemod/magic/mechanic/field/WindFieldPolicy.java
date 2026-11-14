package com.example.examplemod.magic.mechanic.field;
/** Strategy for genuinely varying field geometry and motion decisions. */
public interface WindFieldPolicy {
	double radius();double height();boolean excludesOwner();boolean moving();boolean flame();boolean tempest();boolean vacuum();boolean disperses();boolean violent();
	boolean movesTarget(boolean pulse);
	WindFieldRules.Velocity impulse(double x,double z,double vx,double vy,double vz,boolean player);
}
