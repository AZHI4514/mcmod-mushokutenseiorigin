package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.fire.flash_over.*;
import com.example.examplemod.magic.mechanic.field.ExpiringFieldTick;
class FlashOverModuleTest {
	@Test void firstFrameHasBothFlashAndResidualThenEightResidualPulses(){var s=new FlashOverState(100,260,100,false);var first=s.begin(100);assertTrue(first.flash());assertTrue(first.pulse());assertTrue(s.flashed());assertEquals(120,s.next());assertFalse(s.begin(100).active());int residual=1;for(int t=101;t<260;t++)if(s.begin(t).pulse())residual++;assertEquals(8,residual);}
	@Test void loadedDefaultDoesNotRepeatFlash(){var s=new FlashOverState(0,0,Long.MAX_VALUE,true);assertFalse(s.begin(0).active());}
	@Test void shortenOnlyChangesExpiry(){var s=new FlashOverState(100,260,120,true);s.shorten(200,100);assertEquals(200,s.expiry());assertEquals(120,s.next());assertTrue(s.flashed());assertEquals(100,s.start());}
	private static class Target implements FlashOverContact.Target {
		List<String> calls=new ArrayList<>();boolean claim=true;
		public boolean inside(){return true;}public boolean owner(){return true;}public boolean allowed(){fail();return false;}public boolean visible(){return true;}public void flash(){calls.add("flash");}public boolean claim(){calls.add("claim");return claim;}public void residual(){calls.add("residual");}
	}
	@Test void initialFlashPrecedesClaimAndResidual(){var t=new Target();FlashOverContact.apply(true,true,t);assertEquals(List.of("flash","claim","residual"),t.calls);}
	@Test void refusedCadenceDoesNotSuppressInitialFlash(){var t=new Target();t.claim=false;FlashOverContact.apply(true,true,t);assertEquals(List.of("flash","claim"),t.calls);}
	@Test void initialFlashFailureStopsResidualAttempt(){var t=new Target(){public void flash(){throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->FlashOverContact.apply(true,true,t));assertTrue(t.calls.isEmpty());}
	@Test void residualAndCylinderBoundariesMatchOriginal(){assertEquals(3,FlashOverRules.residual(0));assertEquals(3,FlashOverRules.residual(140));assertEquals(0,FlashOverRules.residual(160));assertTrue(FlashOverRules.contains(16,6,0));assertFalse(FlashOverRules.contains(16,6,.001));}
	private static class World implements ExpiringFieldTick.World {
		List<String> calls=new ArrayList<>();String reject="";boolean pass(String s){calls.add(s);return !reject.equals(s);}
		public boolean statePresent(){return pass("state");}public boolean expired(){return !pass("expiry");}public boolean ownerPresent(){return pass("owner");}public boolean alive(){return pass("alive");}public boolean sameLevel(){return pass("level");}public boolean adopt(){return pass("adopt");}public void removeState(){calls.add("remove");}public void discard(){calls.add("discard");}public void update(){calls.add("update");}
	}
	@Test void sharedExpiryGatePreservesLazyReadAndRemovalOrder(){var checks=List.of("state","expiry","owner","alive","level","adopt");for(int i=0;i<checks.size();i++){var w=new World();w.reject=checks.get(i);ExpiringFieldTick.tick(w);var expected=new ArrayList<>(checks.subList(0,i+1));expected.add("remove");expected.add("discard");assertEquals(expected,w.calls);}var valid=new World();ExpiringFieldTick.tick(valid);assertEquals("update",valid.calls.getLast());}
}
