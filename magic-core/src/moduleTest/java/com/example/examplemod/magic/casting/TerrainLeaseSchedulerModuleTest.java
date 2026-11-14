package com.example.examplemod.magic.casting;
import com.example.examplemod.magic.domain.runtime.TickQuota;
import com.example.examplemod.magic.feature.terrain.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class TerrainLeaseSchedulerModuleTest {
	private static class World implements TerrainLeaseScheduler.Restoration<Integer>, TerrainLeaseScheduler.Assembly<Integer> {
		final List<String> events = new ArrayList<>();
		public boolean dueAndLoaded(Integer n,long now){return n!=0;}
		public void restoreIfUnchanged(Integer n){events.add("r"+n);}
		public boolean expired(Integer n,long now){return n<0;}
		public boolean ready(Integer n){return n!=0;}
		public boolean placeIfValid(Integer n){events.add("p"+n);return n!=1;}
	}
	@Test void quotaIsPerInstanceAndResetsOnlyOnTickChange(){
		var a=new TickQuota(400);var b=new TickQuota(400);
		assertTrue(a.reserve(5,399));assertFalse(a.reserve(5,2));assertEquals(1,a.available(5));
		assertEquals(400,b.available(5));assertEquals(400,a.available(6));assertFalse(a.reserve(6,-1));
	}
	@Test void restorationPrecedesAssemblyAndDeferredAndExpiredJobsDoNotConsumeWork(){
		var world=new World();var quota=new TickQuota(3);var leases=new ArrayList<>(List.of(0,2));var jobs=new ArrayList<>(List.of(-1,0,1,2,3));int[] dirty={0};
		TerrainLeaseScheduler.tick(8,quota,leases,jobs,world,world,()->dirty[0]++);
		assertEquals(List.of("r2","p1","p2"),world.events);assertEquals(List.of(0),leases);assertEquals(List.of(0,3),jobs);assertEquals(0,quota.available(8));assertEquals(4,dirty[0]);
	}
	@Test void twelveSuccessfulPlacementsCapDoesNotCountRejectedAttempts(){
		var world=new World();var jobs=new ArrayList<Integer>();for(int i=1;i<=20;i++)jobs.add(i);var q=new TickQuota(400);
		TerrainLeaseScheduler.tick(0,q,new ArrayList<Integer>(),jobs,world,world,()->{});
		assertEquals(13,world.events.size());assertEquals(14,jobs.getFirst());assertEquals(387,q.available(0));
	}
	@Test void exhaustedQuotaDoesNotEvenDrainExpiredJobs(){
		var w=new World();var jobs=new ArrayList<>(List.of(-1,2));var q=new TickQuota(0);
		TerrainLeaseScheduler.tick(0,q,new ArrayList<Integer>(),jobs,w,w,()->fail("dirty"));assertEquals(List.of(-1,2),jobs);assertTrue(w.events.isEmpty());
	}
}
