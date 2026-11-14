package com.example.examplemod.magic.spell.holy.exorcist_burst;
import com.example.examplemod.magic.mechanic.targeting.HolyVolumePolicy;
public final class ExorcistBurstPolicy implements HolyVolumePolicy {
	public boolean singleTarget(){return false;}public boolean endpointBurst(){return true;}public boolean visibleBeam(){return false;}public double beamSpread(){return .035;}public double burstSpread(){return 1.5;}
	public boolean shape(double x,double y,double z,double ax,double ay,double az,double length){return x*x+y*y+z*z<=16;}
}
