package com.example.examplemod.magic.mechanic.projectile;

public final class CarrierHit {
	public interface Target {
		boolean allowed();
		boolean hurt();
		void effects();
		void afterHit();
		void postHit();
	}
	public static void apply(Target target) {
		if(!target.allowed()||!target.hurt())return;
		target.effects();
		target.afterHit();
		target.postHit();
	}
	private CarrierHit() { }
}
