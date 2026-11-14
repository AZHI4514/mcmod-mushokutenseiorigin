package com.example.examplemod.magic.spell.special.teleportation_magic;
public final class TeleportRules {
	public static boolean validName(String name){return name.matches("[a-zA-Z0-9_-]{1,32}");}
	public static int extra(boolean sameDimension,double distance){return sameDimension?(int)Math.ceil(distance/16)*2:400;}
	private TeleportRules() { }
}
