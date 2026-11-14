package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.feature.input.CastingInputState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CastingInputModuleTest {
	private static java.util.List<String> actions(CastingInputState s,boolean down,boolean mode,String id){return s.sample(down,mode,id).stream().map(CastingInputState.Action::action).toList();}
	@Test void requiresReleaseArmsOnceAndRepeatsEveryTwoTicks(){
		var state=new CastingInputState();assertTrue(actions(state,true,false,"fireball").isEmpty());
		assertTrue(actions(state,false,false,"fireball").isEmpty());assertEquals(java.util.List.of("arm","cast"),actions(state,true,false,"fireball"));
		assertTrue(actions(state,true,false,"fireball").isEmpty());assertEquals(java.util.List.of("cast"),actions(state,true,false,"fireball"));
		assertEquals(java.util.List.of("release"),actions(state,false,false,"fireball"));assertTrue(actions(state,false,false,"fireball").isEmpty());
	}
	@Test void interruptionRequiresReleaseAndNeverProducesSuccessfulRelease(){
		var state=new CastingInputState();state.sample(false,false,"");state.sample(true,false,"water_ball");
		assertTrue(state.interrupt());assertFalse(state.interrupt());assertTrue(state.mustRelease());assertFalse(state.armed());
		assertTrue(actions(state,true,false,"water_ball").isEmpty());assertTrue(actions(state,false,false,"water_ball").isEmpty());
		assertEquals(java.util.List.of("arm","cast"),actions(state,true,false,"water_ball"));state.disconnect();assertTrue(state.mustRelease());assertFalse(state.armed());
	}
	@Test void modeSelectionLocksUntilReleaseAndCannotReplaceArmedCast(){
		var state=new CastingInputState();state.sample(false,false,"gravity_magic");
		assertEquals(java.util.List.of("mode"),actions(state,true,true,"gravity_magic"));assertTrue(actions(state,true,true,"gravity_magic").isEmpty());
		state.sample(false,false,"");assertEquals(java.util.List.of("arm"),actions(state,true,false,""));
		assertEquals(java.util.List.of("cast"),actions(state,true,true,"gravity_magic"));assertTrue(state.armed());
	}
}
