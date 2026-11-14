package com.example.examplemod.magic.mechanic.field;
/** Fog merging preserves the strongest active range and extends the remaining lifetime. */
public final class FogSight {
	public interface State {int remaining();int range();void range(int value);void color(int value);void linger(int value);void remaining(int value);}
	public static void apply(State state,int range,int linger,int color){
		int left=state.remaining(),previous=state.range();
		if(left<=0||range<=previous){state.range(range);state.color(color);state.linger(linger);}
		state.remaining(Math.max(left,linger));
	}
	public interface Target {boolean attackerPresent();boolean targetPresent();boolean retainedTarget();int remaining();int range();double squaredDistance();}
	public static boolean mayAcquire(Target target){
		if(!target.attackerPresent()||!target.targetPresent()||target.retainedTarget())return true;
		int left=target.remaining(),range=target.range();return left<=0||target.squaredDistance()<=range*range;
	}
	private FogSight() { }
}
