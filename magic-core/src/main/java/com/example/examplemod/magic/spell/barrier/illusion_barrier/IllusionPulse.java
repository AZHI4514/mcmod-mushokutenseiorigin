package com.example.examplemod.magic.spell.barrier.illusion_barrier;
public final class IllusionPulse {
	public interface World {boolean due();boolean pay();void discard();void schedule();void visual();}
	public static void tick(World w){if(w.due()){if(!w.pay()){w.discard();return;}w.schedule();}w.visual();}
	private IllusionPulse() { }
}
