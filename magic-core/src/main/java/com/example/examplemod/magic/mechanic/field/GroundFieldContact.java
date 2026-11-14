package com.example.examplemod.magic.mechanic.field;
/** A contact is claimed before damage, including refused damage, exactly as in saved fields. */
public final class GroundFieldContact {
	public interface Target {boolean eligible();boolean inside();boolean surface();boolean visible();void continuous();boolean attackDue();boolean claim();boolean hurt();void afterDamage();void hook();}
	public static void apply(Target t){if(!t.eligible()||!t.inside()||!t.surface()||!t.visible())return;t.continuous();if(!t.attackDue()||!t.claim()||!t.hurt())return;t.afterDamage();t.hook();}
	private GroundFieldContact() { }
}
