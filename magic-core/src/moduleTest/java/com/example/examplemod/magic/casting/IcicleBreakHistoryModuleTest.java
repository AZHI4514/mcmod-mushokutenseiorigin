package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.projectile.ImpactHistory;
import com.example.examplemod.magic.mechanic.projectile.PiercingFlightRules;
class IcicleBreakHistoryModuleTest {
	@Test void claimedAttemptSurvivesSnapshotReloadAndOtherShotsStayIndependent(){
		var id=UUID.randomUUID();var shot=new ImpactHistory();assertTrue(shot.claim(id));
		var restored=new ImpactHistory(shot.snapshot());assertFalse(restored.claim(id));assertFalse(PiercingFlightRules.candidate(1,3,4,restored.contains(id)));
		assertTrue(new ImpactHistory().claim(id));assertThrows(UnsupportedOperationException.class,()->shot.snapshot().clear());
	}
	@Test void stepEndContactClaimPrecedesDamageAndCannotRepeatAfterFailedDamage(){
		var history=new ImpactHistory();var id=UUID.randomUUID();int attempts=0;
		for(int tick=0;tick<3;tick++)if(PiercingFlightRules.candidate(3,4,Double.POSITIVE_INFINITY,history.contains(id))&&PiercingFlightRules.contactDue(3,3,history.contains(id))) {history.claim(id);attempts++;}
		assertEquals(1,attempts);
	}
}
