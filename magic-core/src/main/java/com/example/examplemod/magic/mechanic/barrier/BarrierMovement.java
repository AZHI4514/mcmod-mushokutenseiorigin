package com.example.examplemod.magic.mechanic.barrier;
public final class BarrierMovement {
	public interface Field {boolean active();boolean holdsBody();boolean crosses();double crossing();void stopLiving();}
	public static double clip(Field f){if(!f.active()||!f.holdsBody()||!f.crosses())return Double.NaN;double crossing=f.crossing();f.stopLiving();return Math.max(0,crossing-.001);}
	private BarrierMovement() { }
}
