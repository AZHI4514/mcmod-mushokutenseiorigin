package com.example.examplemod.magic.spell.wind.tornado;
import com.example.examplemod.magic.mechanic.field.*;
public final class TornadoPolicy implements WindFieldPolicy {
	public double radius(){return 5;}public double height(){return 20;}public boolean excludesOwner(){return false;}public boolean moving(){return true;}public boolean flame(){return false;}public boolean tempest(){return false;}public boolean vacuum(){return false;}public boolean disperses(){return false;}public boolean violent(){return false;}
	public boolean movesTarget(boolean pulse){return pulse;}
	public WindFieldRules.Velocity impulse(double x,double z,double vx,double vy,double vz,boolean player){return WindFieldRules.impulse(x,z,vx,vy,vz,true,false,player);}
}
