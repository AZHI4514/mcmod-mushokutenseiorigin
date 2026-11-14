package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.earth.earth_lance.EarthLanceSelection;
class EarthLanceSelectionModuleTest {
 @Test void selfSupportWinsWithoutQueryingLivingTarget(){assertEquals(Optional.of("self"),EarthLanceSelection.select(false,()->Optional.of("self"),()->{fail();return Optional.empty();}));}
 @Test void noSupportQueriesTargetAfterSupport(){var calls=new ArrayList<String>();assertEquals(Optional.of("target"),EarthLanceSelection.select(false,()->{calls.add("self");return Optional.empty();},()->{calls.add("target");return Optional.of("target");}));assertEquals(List.of("self","target"),calls);}
 @Test void legacyTargetEntryNeverQueriesSupport(){assertEquals(Optional.of("target"),EarthLanceSelection.select(true,()->{fail();return Optional.empty();},()->Optional.of("target")));}
 @Test void noTargetRejectsAndQueriesRemainIndependent(){assertTrue(EarthLanceSelection.select(false,()->Optional.empty(),()->Optional.empty()).isEmpty());assertEquals(Optional.of("next"),EarthLanceSelection.select(false,()->Optional.empty(),()->Optional.of("next")));}
}
