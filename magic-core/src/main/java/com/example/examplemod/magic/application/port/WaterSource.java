package com.example.examplemod.magic.application.port;
/** Waterfall world operations. False generation commits no effect; stop never removes permanent water. */
public interface WaterSource {
	boolean begin(float yaw,float pitch);
	boolean targetAvailable(float yaw,float pitch);
	boolean generate(float yaw,float pitch);
	void renew();
}
