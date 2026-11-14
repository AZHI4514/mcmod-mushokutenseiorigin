package com.example.examplemod.magic.application.casting;
import java.util.function.BooleanSupplier;
import com.example.examplemod.magic.application.port.ChannelDebt;
import com.example.examplemod.magic.application.port.PlayerStore;
import com.example.examplemod.magic.mechanic.channel.ChannelCost;
/** Eligibility belongs to the channel controller. This use case owns only fractional payment and entry rollback. */
public final class ChannelPayment {
	private final PlayerStore players;
	private final ChannelDebt debt;
	public ChannelPayment(PlayerStore players,ChannelDebt debt) { this.players=players;this.debt=debt; }
	public boolean pay(int rate) {
		var state=players.read();var cost=ChannelCost.calculate(debt.read(),rate);
		if(state.mana()<cost.debit())return false;
		players.write(state.spendMana(cost.debit()));debt.write(cost.debt());return true;
	}
	/** False means no world effect committed. Exceptions keep payment, matching the legacy irreversible boundary. */
	public boolean begin(int rate,BooleanSupplier firstEffect) {
		var before=players.read();int oldDebt=debt.read();
		if(!pay(rate))return false;
		if(firstEffect.getAsBoolean())return true;
		players.write(before);debt.write(oldDebt);return false;
	}
}
