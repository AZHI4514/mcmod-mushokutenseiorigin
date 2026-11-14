package com.example.examplemod.magic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.earth.stone_cannon.ChargeSession;
class ChargeSessionTest {
	@Test void eleventhMissingHeartbeatExpiresAndRefreshRestartsCounter() {
		var session=new ChargeSession();for(int i=0;i<10;i++)assertFalse(session.expired(true));assertTrue(session.expired(true));session.heartbeat();assertFalse(session.expired(true));assertTrue(session.expired(false));
	}
	@Test void independentSessionsCannotShareElapsedCharge() {
		var first=new ChargeSession();var second=new ChargeSession();for(int i=0;i<20;i++)first.advance(1000,10);assertEquals(21,first.advance(1000,10).ticks());assertEquals(1,second.advance(1000,10).ticks());
	}
}
