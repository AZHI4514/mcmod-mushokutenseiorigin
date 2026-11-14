package com.example.examplemod.magic.spell.wind.vacuum_storm;
import com.example.examplemod.magic.mechanic.field.*;
public final class VacuumStormPolicy implements WindFieldPolicy {
	public double radius(){return 12;}public double height(){return 12;}public boolean excludesOwner(){return false;}public boolean moving(){return false;}public boolean flame(){return false;}public boolean tempest(){return false;}public boolean vacuum(){return true;}public boolean disperses(){return false;}public boolean violent(){return false;}
	public boolean movesTarget(boolean pulse){return false;}
	public WindFieldRules.Velocity impulse(double x,double z,double vx,double vy,double vz,boolean player){return new WindFieldRules.Velocity(vx,vy,vz);}
}
