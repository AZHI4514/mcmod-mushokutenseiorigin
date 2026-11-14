package com.example.examplemod.magic.mechanic.targeting;
/** Opening damage must succeed before the optional nova knockback. */
public final class HolyOpeningHit {
	public interface Target {boolean inside();boolean visible();boolean hurt();void afterDamage();}
	public static void apply(Target target){if(target.inside()&&target.visible()&&target.hurt())target.afterDamage();}
	private HolyOpeningHit() { }
}
