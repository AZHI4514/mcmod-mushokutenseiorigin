package com.example.examplemod.client.magic;

/** Rank-driven visual complexity only; independent of spell damage and cast timing. */
final class SpellParticleDensity {
	static final int HAND_BUDGET = 192, WAKE_BUDGET = 256, IMPACT_BUDGET = 512;
	record Detail(int gather, int sparks, int haze, int wake, float radius) { }
	record Offset(double x, double y, double z) { }
	static Detail of(int rank) {
		int tier = Math.clamp(rank, 1, 7);
		return new Detail(6 * tier, 2 * tier, tier == 1 ? 1 : tier + 1, 5 + 5 * tier, .42F + .2F * tier);
	}
	static float chantPulse(float time) {
		float wave=.5F+.5F*(float)Math.sin(time*.18F);
		return .12F+.88F*wave*wave;
	}
	static int impactCount(int rank, int age) {
		int tier=Math.clamp(rank,1,7);
		if(age==1)return 32+64*tier;
		if(age==4)return 16+32*tier;
		if(age==8)return 16+24*tier;
		if(age<=12)return 6+12*tier;
		if(age<=24)return age%2==0?6*tier:0;
		return age%3==0?3*tier:0;
	}
	/** Independent uniform samples fill a sphere's volume, rather than its axis or surface. */
	static Offset volume(double u, double v, double w, double radius) {
		double y = 2 * u - 1, angle = v * Math.PI * 2;
		double r = radius * Math.cbrt(w), horizontal = Math.sqrt(Math.max(0, 1 - y * y));
		return new Offset(Math.cos(angle) * horizontal * r, y * r, Math.sin(angle) * horizontal * r);
	}
	private SpellParticleDensity() { }
}
