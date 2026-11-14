package com.example.examplemod.magic.spell.holy.exorcist_judge;
import com.example.examplemod.magic.mechanic.targeting.HolyInstantPolicy;
public final class ExorcistJudgePolicy implements HolyInstantPolicy {
	public boolean singleTarget(){return true;}public boolean endpointBurst(){return false;}public boolean visibleBeam(){return true;}public double beamSpread(){return .035;}public double burstSpread(){return .4;}
}
