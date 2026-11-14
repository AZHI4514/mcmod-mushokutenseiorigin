package com.example.examplemod.magic.mechanic.barrier;
public final class BarrierDamage {
	public interface Field {boolean active();boolean acceptsKind();boolean crosses();boolean blocksDamage();boolean absorb();void discard();void visual();}
	public static boolean intercept(Field f){if(!f.active()||!f.acceptsKind()||!f.crosses()||!f.blocksDamage())return false;if(f.absorb())f.discard();f.visual();return true;}
	private BarrierDamage() { }
}
