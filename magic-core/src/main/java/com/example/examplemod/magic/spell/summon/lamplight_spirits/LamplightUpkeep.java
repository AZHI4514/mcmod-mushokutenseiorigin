package com.example.examplemod.magic.spell.summon.lamplight_spirits;
public final class LamplightUpkeep {
	public record Charge(int fee,int credit) { }
	public static Charge next(int credit){int fee=0;if(credit==0){fee=1;credit=5;}return new Charge(fee,credit-1);}
	private LamplightUpkeep() { }
}
