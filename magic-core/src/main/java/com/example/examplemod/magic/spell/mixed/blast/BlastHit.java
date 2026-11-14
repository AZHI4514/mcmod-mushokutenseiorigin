package com.example.examplemod.magic.spell.mixed.blast;

public final class BlastHit {
	public interface Target {
		boolean hurt();
		boolean impulseImmune();
		void impulse(double strength,boolean useAim);
		void afterHit();
	}
	public static void apply(Target target,double offsetSquared) {
		if (!target.hurt()) return;
		if (!target.impulseImmune()) target.impulse(.6,offsetSquared<1E-6);
		target.afterHit();
	}
	private BlastHit() { }
}
