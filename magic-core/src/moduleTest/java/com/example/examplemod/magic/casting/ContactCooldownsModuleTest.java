package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.field.ContactCooldowns;
import com.example.examplemod.magic.domain.model.Vec3d;
import com.example.examplemod.magic.spell.fire.flamethrower.FlamethrowerHit;
class ContactCooldownsModuleTest {
	@Test void twentiethTickIsInclusiveAndBlockedAttemptsDoNotExtendWindow(){
		var c=new ContactCooldowns(20);var source=UUID.randomUUID();var victim=UUID.randomUUID();
		assertTrue(c.claim(source,victim,100));assertFalse(c.claim(source,victim,119));assertTrue(c.claim(source,victim,120));assertFalse(c.claim(source,victim,139));assertTrue(c.claim(source,victim,140));
	}
	@Test void sourceAndVictimIdentitiesAreIndependentAndClearIsScoped(){
		var c=new ContactCooldowns(20);var a=UUID.randomUUID();var b=UUID.randomUUID();var x=UUID.randomUUID();var y=UUID.randomUUID();
		assertTrue(c.claim(a,x,100));assertTrue(c.claim(a,y,100));assertTrue(c.claim(b,x,100));c.clear(a);
		assertTrue(c.claim(a,x,101));assertFalse(c.claim(b,x,101));
	}
	@Test void backwardsTimeDoesNotExpireAndPruneDoesNotResetLiveWindows(){
		var c=new ContactCooldowns(20);var a=UUID.randomUUID();var x=UUID.randomUUID();assertTrue(c.claim(a,x,100));
		c.prune(80);assertFalse(c.claim(a,x,80));c.prune(119);assertFalse(c.claim(a,x,119));c.prune(120);assertTrue(c.claim(a,x,120));
	}
	@Test void runtimeInstancesDoNotShareEvenIdenticalIdentityKeys(){
		var a=new ContactCooldowns(20);var b=new ContactCooldowns(20);var source=UUID.randomUUID();var victim=UUID.randomUUID();
		assertTrue(a.claim(source,victim,0));assertTrue(b.claim(source,victim,0));a.clear(source);assertTrue(a.claim(source,victim,1));assertFalse(b.claim(source,victim,1));
	}
	@Test void invisibleTargetsNeverClaimAndRejectedDamageStillConsumesContact(){
		var c=new ContactCooldowns(20);var source=UUID.randomUUID();var victim=UUID.randomUUID();var effects=new ArrayList<String>();
		var visible=new boolean[]{false};var now=new long[]{100};
		var targets=new FlamethrowerHit.Targets<UUID>(){
			public Iterable<UUID> candidates(){return List.of(victim);}
			public Vec3d delta(UUID target){return new Vec3d(0,0,2);}
			public boolean visible(UUID target){return visible[0];}
			public boolean claim(UUID target){return c.claim(source,target,now[0]);}
			public void damage(UUID target){effects.add("attempt");}
		};
		FlamethrowerHit.apply(new Vec3d(0,0,1),targets);assertTrue(effects.isEmpty());visible[0]=true;
		FlamethrowerHit.apply(new Vec3d(0,0,1),targets);FlamethrowerHit.apply(new Vec3d(0,0,1),targets);assertEquals(List.of("attempt"),effects);
		now[0]=120;FlamethrowerHit.apply(new Vec3d(0,0,1),targets);assertEquals(List.of("attempt","attempt"),effects);
	}
}
