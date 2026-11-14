package com.example.examplemod.magic.spell.mixed.bora_blast;

/** Status is read after damage and impulse, never snapshotted before them. */
public final class BoraBlastHit {
	public interface Target {
		boolean hurt();
		boolean impulseImmune();
		void impulse(double strength,boolean useAim);
		boolean warm();
		void removeWarm();
		boolean canFreeze();
		void freeze(int ticks);
		boolean wet();
		void slow(int ticks,int amplifier);
		void afterHit();
	}
	public static void apply(Target target,double offsetSquared) {
		if (!target.hurt()) return;
		if (!target.impulseImmune()) target.impulse(.7,offsetSquared<1E-6);
		if (target.warm()) target.removeWarm();
		else if (target.canFreeze()) target.freeze(300);
		if (target.wet()) target.slow(100,1);
		target.afterHit();
	}
	private BoraBlastHit() { }
}
