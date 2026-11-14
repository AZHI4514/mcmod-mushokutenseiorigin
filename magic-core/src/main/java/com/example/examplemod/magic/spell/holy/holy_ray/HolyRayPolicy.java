package com.example.examplemod.magic.spell.holy.holy_ray;
import com.example.examplemod.magic.mechanic.targeting.HolyVolumePolicy;
import com.example.examplemod.magic.mechanic.targeting.HolyGeometry;
public final class HolyRayPolicy implements HolyVolumePolicy {
	public boolean singleTarget(){return false;}
	public boolean endpointBurst(){return false;}
	public boolean visibleBeam(){return true;}
	public double beamSpread(){return .55;}
	public double burstSpread(){return .4;}
	public boolean shape(double x,double y,double z,double ax,double ay,double az,double length){return HolyGeometry.column(x,y,z,ax,ay,az,length);}
}
