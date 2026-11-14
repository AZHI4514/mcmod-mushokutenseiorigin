package com.example.examplemod.magic.application.port;
/** Server-thread persistent reservation balance, separate from spendable player mana. */
public interface ManaReservation {
	int reserved();
	void write(int paid,int initialBudget);
}
