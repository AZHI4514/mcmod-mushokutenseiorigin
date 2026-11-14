package com.example.examplemod.magic.spell.wind.sonic_blast;

public final class SonicBlastHit {
	public interface Target {
		boolean hurt();
		boolean impulseImmune();
		void impulse(double strength,boolean useAim);
		void afterHit();
	}
	public static void apply(Target target,double offsetSquared) {
		if (!target.hurt()) return;
		if (!target.impulseImmune()) target.impulse(1.2,offsetSquared<.001);
		target.afterHit();
	}
	private SonicBlastHit() { }
}
