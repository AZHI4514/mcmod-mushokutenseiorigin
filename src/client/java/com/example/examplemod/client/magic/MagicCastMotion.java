package com.example.examplemod.client.magic;

/** Continuous, small offsets around the existing casting pose; time includes render interpolation. */
public final class MagicCastMotion {
	public static float pitch(float time) { return .065F*(float)Math.sin(time*.13F)+.016F*(float)Math.sin(time*.29F); }
	public static float yaw(float time) { return .035F*(float)Math.sin(time*.11F+.7F); }
	public static float roll(float time) { return .045F*(float)Math.sin(time*.16F+1.2F); }
	public static float lift(float time) { return .006F*(float)Math.sin(time*.13F); }
	public static float handSide(float time) { return .018F*(float)Math.sin(time*.11F+.7F); }
	public static float handLift(float time) { return .018F*(float)Math.sin(time*.13F); }
	private MagicCastMotion() { }
}
