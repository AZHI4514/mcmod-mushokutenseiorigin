package com.example.examplemod.magic.spell.special.saint_dragon_battle_aura;
public final class DragonAuraTick {
	public interface World {boolean ended();boolean alive();boolean active();void clearActive();void recovery();void endNow();void removeResistance();void activate();boolean paymentDue();void schedule();boolean pay();void addResistance();void visual();}
	public static void tick(World w){
		if(w.ended()||!w.alive()){if(w.active()){w.clearActive();w.recovery();w.endNow();}w.removeResistance();return;}
		if(!w.active())w.activate();
		if(w.paymentDue()){w.schedule();if(!w.pay()){w.endNow();w.recovery();w.removeResistance();return;}}
		w.addResistance();w.visual();
	}
	private DragonAuraTick() { }
}
