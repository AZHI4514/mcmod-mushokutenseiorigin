package com.example.examplemod.magic.spell.earth.terra_fall;
import com.example.examplemod.magic.mechanic.field.GroundFieldPolicy;
public final class TerraFallPolicy implements GroundFieldPolicy {
	public double radius(){return 20;}public double height(){return 12;}
	public int damagePulse(long age){return age>=20&&age<120?(int)((age-20)/40):-1;}
	public boolean warning(long age){return age<20||age>=40&&age<60||age>=80&&age<100;}
	public double visualLift(){return 7;}
}
