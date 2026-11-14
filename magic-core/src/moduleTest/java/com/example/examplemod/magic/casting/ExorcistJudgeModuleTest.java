package com.example.examplemod.magic.casting;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.targeting.*;
import com.example.examplemod.magic.spell.holy.exorcist_judge.ExorcistJudgePolicy;
class ExorcistJudgeModuleTest {
	@Test void singleTargetPolicyDoesNotRequireAreaGeometry(){HolyInstantPolicy p=new ExorcistJudgePolicy();assertTrue(p.singleTarget());assertFalse(p instanceof HolyVolumePolicy);assertFalse(p.endpointBurst());assertTrue(p.visibleBeam());assertEquals(.035,p.beamSpread());assertEquals(.4,p.burstSpread());}
	@Test void strictNearestSelectionKeepsFirstEqualCandidate(){double nearest=10;int selected=-1;double[] contacts={5,5,8,Double.NaN};for(int i=0;i<contacts.length;i++)if(HolyNearest.nearer(contacts[i],nearest)){nearest=contacts[i];selected=i;}assertEquals(0,selected);assertEquals(5,nearest);}
}
