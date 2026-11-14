package com.example.examplemod.magic.spell.fire.magma_gush;
public final class MagmaGushTick {
	public interface World {boolean statePresent();boolean ownerPresent();boolean alive();boolean sameLevel();boolean expired();boolean adopt();void removeState();void discard();boolean begin();float damage();void hitTargets(float damage);}
	public static void tick(World w){if(!w.statePresent()||!w.ownerPresent()||!w.alive()||!w.sameLevel()||w.expired()||!w.adopt()){w.removeState();w.discard();return;}if(!w.begin())return;float damage=w.damage();if(damage!=0)w.hitTargets(damage);}
	private MagmaGushTick() { }
}
