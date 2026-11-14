package com.example.examplemod.magic.spell.fire.exodus_flame;

/** One burst candidate; claiming precedes damage even when vanilla rejects the hit. */
public final class ExodusFlameHit {
	public interface Target {
		boolean previouslyHit();
		double distanceSquared();
		boolean visible();
		boolean owner();
		boolean allowed();
		void claim();
		boolean hurt();
		void selfEffect();
		void otherEffect();
		void afterHit();
	}
	public static void apply(Target target) {
		if (target.previouslyHit() || !ExodusFlameRules.insideBurst(target.distanceSquared()) || !target.visible()) return;
		if (!target.owner() && !target.allowed()) return;
		target.claim();
		if (!target.hurt()) return;
		if (target.owner()) target.selfEffect();
		else target.otherEffect();
		target.afterHit();
	}
	private ExodusFlameHit() { }
}
