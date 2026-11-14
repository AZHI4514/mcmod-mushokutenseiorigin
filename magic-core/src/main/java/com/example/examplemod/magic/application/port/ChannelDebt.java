package com.example.examplemod.magic.application.port;
/** Persistent twentieth-MP credit shared by channels; keeps the existing [-19,0] save contract. */
public interface ChannelDebt {
	int read();
	void write(int debt);
}
