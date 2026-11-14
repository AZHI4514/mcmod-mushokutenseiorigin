package com.example.examplemod.magic.spell.barrier.magic_shield;
public final class MagicShieldDamageGate {
	public interface Hit {boolean magicProjectile();boolean shieldAvailable();void absorb();}
	public static boolean allow(Hit hit){if(!hit.magicProjectile()||!hit.shieldAvailable())return true;hit.absorb();return false;}
	private MagicShieldDamageGate() { }
}
