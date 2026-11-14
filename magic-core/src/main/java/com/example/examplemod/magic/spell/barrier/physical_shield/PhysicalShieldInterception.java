package com.example.examplemod.magic.spell.barrier.physical_shield;
import com.example.examplemod.magic.mechanic.projectile.ShieldInterception;
/** Compatibility type for the physical shield's existing narrow contact adapter. */
public final class PhysicalShieldInterception {
	public interface Contact extends ShieldInterception.Contact { }
	public static boolean apply(Contact contact){return ShieldInterception.apply(contact);}
	private PhysicalShieldInterception() { }
}
