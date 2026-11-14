package com.example.examplemod.magic.spell.fire.apocalypse_flame;
import com.example.examplemod.magic.mechanic.field.GroundFieldPolicy;
public final class ApocalypseFlamePolicy implements GroundFieldPolicy {
	public double radius(){return 48;}public double height(){return 24;}
	public int damagePulse(long age){return age<60||age>360?-1:age==60?0:age>=80?(int)((age-60)/20):-1;}
	public boolean warning(long age){return age<60;}
	public boolean cadenceRequired(int pulse){return pulse>0;}
	public float damage(int pulse,float base){return pulse==0?48:4;}
}
