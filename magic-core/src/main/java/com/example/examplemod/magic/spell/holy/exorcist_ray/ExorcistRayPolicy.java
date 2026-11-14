package com.example.examplemod.magic.spell.holy.exorcist_ray;
import com.example.examplemod.magic.mechanic.targeting.HolyInstantPolicy;
public final class ExorcistRayPolicy implements HolyInstantPolicy {
	public boolean singleTarget(){return true;}public boolean endpointBurst(){return false;}public boolean visibleBeam(){return true;}public double beamSpread(){return .035;}public double burstSpread(){return .4;}
	public static boolean nearer(double candidate,double nearest){return com.example.examplemod.magic.mechanic.targeting.HolyNearest.nearer(candidate,nearest);}
}
