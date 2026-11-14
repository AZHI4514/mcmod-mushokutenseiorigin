package com.example.examplemod.magic.mechanic.summon;
public final class SummonTick {
	public interface World {boolean ownerPresent();boolean sameLevel();boolean ownerAlive();boolean summonAlive();boolean expired();boolean slotMatches();void discard();boolean pulseDue();void schedule();boolean pay();void act(boolean pulse);}
	public static void tick(World w){if(!w.ownerPresent()||!w.sameLevel()||!w.ownerAlive()||!w.summonAlive()||w.expired()||!w.slotMatches()){w.discard();return;}boolean pulse=w.pulseDue();if(pulse){w.schedule();if(!w.pay()){w.discard();return;}}w.act(pulse);}
	private SummonTick() { }
}
