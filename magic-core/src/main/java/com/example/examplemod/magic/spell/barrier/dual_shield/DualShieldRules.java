package com.example.examplemod.magic.spell.barrier.dual_shield;
public final class DualShieldRules {
	public static boolean intercepts(int mode,boolean magic){return !(mode==0&&magic||mode==1&&!magic);}
	private DualShieldRules() { }
}
