package com.example.examplemod.magic.spell.fire.flame_pillar;
/** Absolute world-time task. Initial damage belongs to ignition, not this delayed task. */
public final class FlamePillarBurn {
	public interface Target {
		boolean alive();
		boolean sameLevel();
		void damage();
	}
	private final long start;
	public FlamePillarBurn(long start) { this.start=start; }
	/** True removes only this task; existing vanilla fire is not extinguished. */
	public boolean tick(long now,Target target) {
		long age=now-start;
		if(!target.alive()||!target.sameLevel()||age>=FlamePillarSpec.DURATION)return true;
		if(age>0&&age%FlamePillarSpec.PULSE_TICKS==0)target.damage();
		return false;
	}
}
