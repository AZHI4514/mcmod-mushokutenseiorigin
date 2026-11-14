package com.example.examplemod.magic.spell.wind.sky_tempest;
import com.example.examplemod.magic.mechanic.field.*;
public final class SkyTempestPolicy implements WindFieldPolicy {
	public double radius(){return 24;}public double height(){return 40;}public boolean excludesOwner(){return false;}public boolean moving(){return false;}public boolean flame(){return false;}public boolean tempest(){return true;}public boolean vacuum(){return false;}public boolean disperses(){return true;}public boolean violent(){return false;}
	public boolean movesTarget(boolean pulse){return true;}
	public static float damage(double x,double z,float outerDamage){return x*x+z*z<=64?7:outerDamage;}
	public WindFieldRules.Velocity impulse(double x,double z,double vx,double vy,double vz,boolean player){return WindFieldRules.impulse(x,z,vx,vy,vz,false,true,player);}
}
