package com.example.examplemod.magic.spell.mixed.flame_tornado;
import com.example.examplemod.magic.mechanic.field.*;
public final class FlameTornadoPolicy implements WindFieldPolicy {
	public double radius(){return 5;}public double height(){return 18;}public boolean excludesOwner(){return false;}public boolean moving(){return true;}public boolean flame(){return true;}public boolean tempest(){return false;}public boolean vacuum(){return false;}public boolean disperses(){return false;}public boolean violent(){return false;}
	public boolean movesTarget(boolean pulse){return pulse;}
	public static double vertical(double original,boolean player){return Math.min(player?.35:Double.MAX_VALUE,original+.06);}
	public WindFieldRules.Velocity impulse(double x,double z,double vx,double vy,double vz,boolean player){return WindFieldRules.impulse(x,z,vx,vy,vz,true,false,player);}
}
