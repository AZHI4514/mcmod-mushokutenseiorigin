package com.example.examplemod.magic.mechanic.channel;
/** Fixed-point accounting in twentieths of MP. Negative debt is prepaid credit, not refundable whole MP. */
public record ChannelCost(int debit,int debt) {
	public static ChannelCost calculate(int previousDebt,int rate) {
		if(previousDebt < -19 || previousDebt > 0 || rate < 0)throw new IllegalArgumentException("Invalid channel balance");
		int debit=(int)(((long)previousDebt+rate+19)/20);
		return new ChannelCost(debit,remainder(previousDebt,rate,debit));
	}
	public static int remainder(int previousDebt,int rate,int debit) {
		return (int)((long)previousDebt+rate-(long)debit*20);
	}
}
