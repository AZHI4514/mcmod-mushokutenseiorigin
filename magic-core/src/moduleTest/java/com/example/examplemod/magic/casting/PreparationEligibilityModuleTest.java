package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.application.casting.PreparationEligibility;
class PreparationEligibilityModuleTest {
	static class Player implements PreparationEligibility.Player {
		final List<Integer> reads=new ArrayList<>();int denied=-1;boolean throwsHook;
		boolean read(int i){reads.add(i);return denied!=i;}
		public boolean counterReady(){return read(0);}
		public boolean silenced(){return !read(1);}
		public boolean alive(){return read(2);}
		public boolean spectator(){return !read(3);}
		public boolean usingItem(){return !read(4);}
		public boolean sleeping(){return !read(5);}
		public boolean inventoryOpen(){return read(6);}
		public boolean cooldownReady(){return read(7);}
		public boolean selected(){return read(8);}
		public boolean learned(){return read(9);}
		public boolean loaded(){return read(10);}
		public boolean enoughMana(){return read(11);}
		public boolean hookAllows(){if(throwsHook)throw new IllegalStateException();return read(12);}
	}
	@Test void acceptedReadsInLegacyOrder(){var p=new Player();assertTrue(PreparationEligibility.allows(p));assertEquals(java.util.stream.IntStream.range(0,13).boxed().toList(),p.reads);}
	@Test void everyDeniedStageShortCircuitsLaterWorldReads(){for(int i=0;i<13;i++){var p=new Player();p.denied=i;assertFalse(PreparationEligibility.allows(p));assertEquals(java.util.stream.IntStream.rangeClosed(0,i).boxed().toList(),p.reads);}}
	@Test void insufficientManaDoesNotFireHook(){var p=new Player();p.denied=11;p.throwsHook=true;assertFalse(PreparationEligibility.allows(p));}
	@Test void eventExceptionPropagates(){var p=new Player();p.throwsHook=true;assertThrows(IllegalStateException.class,()->PreparationEligibility.allows(p));assertEquals(12,p.reads.size());}
}
