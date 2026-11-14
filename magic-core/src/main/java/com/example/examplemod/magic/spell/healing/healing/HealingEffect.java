package com.example.examplemod.magic.spell.healing.healing;
public final class HealingEffect {
	public interface Patient {float health();float maximum();void heal(float amount);void visual();}
	public static boolean apply(Patient p,float healing){float before=p.health(),amount=HealingRules.amount(before,p.maximum(),healing);if(amount<=0)return false;p.heal(amount);if(p.health()<=before)return false;p.visual();return true;}
	private HealingEffect() { }
}
