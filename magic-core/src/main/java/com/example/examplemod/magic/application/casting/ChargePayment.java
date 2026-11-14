package com.example.examplemod.magic.application.casting;
import com.example.examplemod.magic.application.port.*;
import com.example.examplemod.magic.spell.earth.stone_cannon.ChargeStep;
/** Reservation use case. Persistent balance is the authority, so cancellation is idempotent after reconnect. */
public final class ChargePayment {
	private final PlayerStore players;private final ManaReservation reserve;
	public ChargePayment(PlayerStore players,ManaReservation reserve) { this.players=players;this.reserve=reserve; }
	public boolean begin() {
		var state=players.read();if(reserve.reserved()!=0||!state.canCast("stone_cannon",10))return false;
		reserve.write(10,state.mana());players.write(state.spendMana(10));return true;
	}
	public void debit(ChargeStep step,int initialBudget) {
		if(step.debit()==0)return;
		if(step.reserved()!=reserve.reserved()+step.debit())throw new IllegalStateException("Stale charge step");
		players.write(players.read().spendMana(step.debit()));reserve.write(step.reserved(),initialBudget);
	}
	public void cancel() {
		int paid=reserve.reserved();reserve.write(0,0);
		if(paid>0)players.write(players.read().restoreMana(paid));
	}
	public boolean release(PaidProjectile projectile,int recovery) {
		int paid=reserve.reserved();if(paid<10)return false;
		if(!projectile.spawn(paid)) { cancel();return false; }
		reserve.write(0,0);players.write(players.read().withCooldown(recovery));return true;
	}
}
