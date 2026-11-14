package com.example.examplemod.magic.spell.fire.flash_over;
import com.example.examplemod.magic.mechanic.field.BurstFieldContact;
public final class FlashOverContact {
	public interface Target extends BurstFieldContact.Target { }
	public static void apply(boolean flash,boolean pulse,Target t){BurstFieldContact.apply(flash,pulse,t);}
	private FlashOverContact() { }
}
