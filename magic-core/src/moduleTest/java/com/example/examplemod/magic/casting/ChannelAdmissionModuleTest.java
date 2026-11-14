package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.channel.ChannelAdmission;
class ChannelAdmissionModuleTest {
	static class Entry implements ChannelAdmission.Entry {
		final List<String> events=new ArrayList<>();boolean begun=true,registered;
		public boolean begin(){events.add("begin");return begun;}
		public void register(){events.add("register");registered=true;}
		public void emit(){assertTrue(registered);events.add("emit");}
		public void releaseSound(){events.add("sound");}
	}
	@Test void successOrder(){var e=new Entry();assertTrue(ChannelAdmission.apply(e));assertEquals(List.of("begin","register","emit","sound"),e.events);}
	@Test void refusedBeginDoesNotRegister(){var e=new Entry();e.begun=false;assertFalse(ChannelAdmission.apply(e));assertEquals(List.of("begin"),e.events);assertFalse(e.registered);}
	@Test void beginFailureDoesNotCommit(){var e=new Entry(){public boolean begin(){super.begin();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->ChannelAdmission.apply(e));assertFalse(e.registered);}
	@Test void emissionFailureKeepsRegistration(){var e=new Entry(){public void emit(){super.emit();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->ChannelAdmission.apply(e));assertTrue(e.registered);assertEquals(List.of("begin","register","emit"),e.events);}
	@Test void registrationFailureDoesNotEmit(){var e=new Entry(){public void register(){super.register();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->ChannelAdmission.apply(e));assertEquals(List.of("begin","register"),e.events);}
	@Test void soundFailureKeepsCommittedEmission(){var e=new Entry(){public void releaseSound(){super.releaseSound();throw new IllegalStateException();}};assertThrows(IllegalStateException.class,()->ChannelAdmission.apply(e));assertTrue(e.registered);assertEquals(List.of("begin","register","emit","sound"),e.events);}
}
