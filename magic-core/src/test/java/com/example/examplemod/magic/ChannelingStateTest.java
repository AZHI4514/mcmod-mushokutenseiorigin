package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.domain.casting.*;
class ChannelingStateTest {
	@Test void sixTicksRemainChannelingThenSeventhTransitionsToIdle() {
		var session=CastingSession.channeling();
		for(int i=0;i<6;i++) { var step=session.tick(true);assertInstanceOf(Channeling.class,step.next());assertEquals(CastingSession.Signal.NONE,step.signal());assertEquals(0,step.remaining()); }
		var stopped=session.tick(true);assertInstanceOf(Idle.class,stopped.next());assertEquals(CastingSession.Signal.CANCELLED,stopped.signal());
		session.heartbeat();assertEquals(CastingSession.Signal.NONE,session.tick(true).signal());
	}
	@Test void heartbeatRefreshesOnlyActivePhaseAndEligibilityCancelsWithoutReady() {
		var session=CastingSession.channeling();for(int i=0;i<100;i++) { session.heartbeat();assertInstanceOf(Channeling.class,session.tick(true).next()); }
		assertEquals(CastingSession.Signal.CANCELLED,session.tick(false).signal());
		assertEquals(CastingSession.Signal.NONE,session.cancel().signal());assertInstanceOf(Idle.class,session.tick(true).next());
		var explicit=CastingSession.channeling();assertEquals(CastingSession.Signal.CANCELLED,explicit.cancel().signal());assertEquals(CastingSession.Signal.NONE,explicit.tick(true).signal());
	}
}
