package com.example.examplemod.magic.spell.wind.tornado_impact;
public final class TornadoImpactFlight {
	public static boolean expired(int age,int flightTicks,double motionSquared) {
		return age>=flightTicks || motionSquared<1.0E-8;
	}
	public static boolean candidate(double contact,double wall,double fluid) {
		return contact<Math.min(wall,fluid);
	}
	private TornadoImpactFlight() { }
}
