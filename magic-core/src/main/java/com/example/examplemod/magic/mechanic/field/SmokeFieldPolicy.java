package com.example.examplemod.magic.mechanic.field;
/** Independent volume/contact/visual specifications; no game world access. */
public interface SmokeFieldPolicy {
	record Sight(int range,int linger,int color) {}
	record Slowing(int duration,int amplifier,boolean held) {}
	double radius();double height();
	Sight sight();Slowing slowing();
	default boolean launched(){return false;}
	default boolean requiresSky(){return false;}
	default boolean damaging(){return false;}
	int particleCount();
}
