package com.example.examplemod.magic.spell.wind.violent_storm;
import com.example.examplemod.magic.mechanic.field.*;
public final class ViolentStormPolicy implements WindFieldPolicy {
	public double radius(){return 16;}public double height(){return 16;}public boolean excludesOwner(){return true;}public boolean moving(){return false;}public boolean flame(){return false;}public boolean tempest(){return false;}public boolean vacuum(){return false;}public boolean disperses(){return true;}public boolean violent(){return true;}
	public boolean movesTarget(boolean pulse){return pulse;}
	public WindFieldRules.Velocity impulse(double x,double z,double vx,double vy,double vz,boolean player){return WindFieldRules.impulse(x,z,vx,vy,vz,false,false,player);}
}
