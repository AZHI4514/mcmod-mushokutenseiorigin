package com.example.examplemod.magic.spell.water.water_shield;
public final class WaterShieldContact {
	public interface Target {void drag();void markMotion();boolean alreadyContacted();void claim();void wet();}
	public static void apply(Target target){target.drag();target.markMotion();if(!target.alreadyContacted()){target.claim();target.wet();}}
	private WaterShieldContact() { }
}
