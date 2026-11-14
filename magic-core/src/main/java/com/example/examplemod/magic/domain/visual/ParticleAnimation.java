package com.example.examplemod.magic.domain.visual;
/** Pure flipbook particle sampling; the renderer owns only sprites and graphics state. */
public final class ParticleAnimation {
	public static boolean emitGlyph(int tick) { return Math.floorMod(tick,4)==0; }
	public static float progress(int age,float delta,int lifetime) { return Math.clamp((age+delta)/lifetime,0,1); }
	public static float alpha(float progress,boolean haze,boolean accent,boolean glyph) {
		return haze||accent?SpellAtmosphereProfile.envelope(progress)*(haze?.7F:1.7F):(float)Math.sin(progress*Math.PI)*(glyph?.85F:.8F);
	}
	public static float size(float progress,boolean haze,boolean accent,boolean glyph) {
		return haze?.55F+1.35F*SpellAtmosphereProfile.smooth(progress):accent?.65F+.65F*SpellAtmosphereProfile.smooth(progress):glyph?.65F+.35F*Math.min(1,progress*5):.6F+progress*.6F;
	}
	public static double driftX(int age,float drift) { return Math.sin(age*.13+drift)*.0006; }
	public static double driftZ(int age,float drift) { return Math.cos(age*.11+drift)*.0006; }
	public static int frame(float progress) { return Math.min(23,(int)(progress*24)); }
	private ParticleAnimation() {}
}
