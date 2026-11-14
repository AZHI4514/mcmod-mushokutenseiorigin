package com.example.examplemod.magic.spell.wind.tornado_impact;
public final class TornadoImpactHit {
	public interface Target {
		boolean hurt();
		boolean impulseImmune();
		void impulse();
		void knockback();
		void effect();
		void afterHit();
	}
	public static void apply(Target target) {
		if (!target.hurt()) return;
		if (!target.impulseImmune()) target.impulse();
		target.knockback();
		target.effect();
		target.afterHit();
	}
	private TornadoImpactHit() { }
}
