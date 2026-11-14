package com.example.examplemod.magic.spell.mixed.frost_nova;
/** Status snapshots precede damage; rejected damage never consumes either status. */
public final class FrostNovaHit {
	public interface Target {
		boolean wet();
		boolean warm();
		boolean hurt(float damage);
		void removeWet();
		void removeWarm();
		boolean canFreeze();
		void freeze(int ticks);
		boolean player();
		void control(int ticks);
		void afterHit();
	}
	public static void apply(Target target) {
		boolean wet=target.wet(),warm=target.warm();
		if (!target.hurt(FrostNovaRules.damage(wet))) return;
		if (wet) target.removeWet();
		if (warm) target.removeWarm();
		if (!warm) {
			if (target.canFreeze()) target.freeze(FrostNovaRules.FREEZE_TICKS);
			target.control(FrostNovaRules.controlTicks(target.player()));
		}
		target.afterHit();
	}
	private FrostNovaHit() { }
}
