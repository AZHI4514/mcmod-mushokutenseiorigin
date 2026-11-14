package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.spell.barrier.magic_shield.MagicShieldDamageGate;
class MagicShieldModuleTest {
	static class Hit implements MagicShieldDamageGate.Hit {
		final List<String> events=new ArrayList<>();boolean magic=true,available=true;
		public boolean magicProjectile(){events.add("magic");return magic;}
		public boolean shieldAvailable(){events.add("find");return available;}
		public void absorb(){events.add("absorb");}
	}
	@Test void nonMagicNeverLooksUpShield(){var h=new Hit();h.magic=false;assertTrue(MagicShieldDamageGate.allow(h));assertEquals(List.of("magic"),h.events);}
	@Test void missingOrInvalidShieldAllowsDamage(){var h=new Hit();h.available=false;assertTrue(MagicShieldDamageGate.allow(h));assertEquals(List.of("magic","find"),h.events);}
	@Test void absorbsThenRejectsWholeIncomingHit(){var h=new Hit();assertFalse(MagicShieldDamageGate.allow(h));assertEquals(List.of("magic","find","absorb"),h.events);}
	@Test void absorptionExceptionPropagates(){var h=new Hit(){public void absorb(){super.absorb();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->MagicShieldDamageGate.allow(h));assertEquals(List.of("magic","find","absorb"),h.events);}
}
