package com.example.examplemod.magic.mechanic.barrier;
public final class BarrierProjectile {
	public interface Field {boolean active();boolean crosses();boolean accepts();boolean absorb();boolean trident();void reflect();void discardProjectile();void discardField();void visual();}
	public static boolean intercept(Field f){if(!f.active()||!f.crosses()||!f.accepts())return false;boolean broken=f.absorb();if(f.trident())f.reflect();else f.discardProjectile();if(broken)f.discardField();f.visual();return true;}
	private BarrierProjectile() { }
}
