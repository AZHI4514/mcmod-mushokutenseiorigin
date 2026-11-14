package com.example.examplemod.magic.spell.earth.stone_cannon;
import com.example.examplemod.magic.mechanic.channel.ChargeCurve;
/** Pure one-tick charge calculation; balances are read and committed by the server adapter. */
public record ChargeStep(int ticks,int debit,int reserved,boolean release) {
	public static ChargeStep advance(int elapsed,int mana,int reserved) {
		if(elapsed<0||mana<0||reserved<0)throw new IllegalArgumentException("Invalid charge snapshot");
		int next=elapsed==Integer.MAX_VALUE?elapsed:elapsed+1;
		long due=ChargeCurve.extraAfterTicks(next)-ChargeCurve.extraAfterTicks(elapsed);
		int debit=(int)Math.min(due,Math.min(mana,Integer.MAX_VALUE-reserved));
		return new ChargeStep(next,debit,reserved+debit,mana-debit==0);
	}
}
