package com.example.examplemod.magic.mechanic.projectile;
import com.example.examplemod.magic.domain.combat.EffectRules;
public final class ShieldAbsorption {
	public interface Shield {float capacity();void capacity(float value);void show();void discard();}
	public static void apply(float incoming,Shield shield){shield.capacity(EffectRules.shieldAfter(shield.capacity(),incoming));shield.show();if(shield.capacity()<=0)shield.discard();}
	private ShieldAbsorption() { }
}
