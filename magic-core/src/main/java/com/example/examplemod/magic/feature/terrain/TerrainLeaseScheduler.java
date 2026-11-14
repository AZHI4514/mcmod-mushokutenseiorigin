package com.example.examplemod.magic.feature.terrain;
import com.example.examplemod.magic.domain.runtime.TickQuota;

import java.util.List;

/** Queue ordering and bounded scheduling, independent of blocks, servers and persistence codecs. */
public final class TerrainLeaseScheduler {
	public interface Restoration<L> {
		boolean dueAndLoaded(L lease, long now);
		void restoreIfUnchanged(L lease);
	}
	public interface Assembly<J> {
		boolean expired(J job, long now);
		boolean ready(J job);
		boolean placeIfValid(J job);
	}
	public static <L,J> void tick(long now, TickQuota quota, List<L> leases, List<J> pending,
			Restoration<L> restoration, Assembly<J> assembly, Runnable dirty) {
		int budget = quota.available(now);
		var restore = leases.iterator();
		while (restore.hasNext() && budget > 0) {
			var lease = restore.next();
			if (!restoration.dueAndLoaded(lease, now)) continue;
			restoration.restoreIfUnchanged(lease);
			restore.remove(); budget--; quota.consume(1); dirty.run();
		}
		int assembled = 0;
		var jobs = pending.iterator();
		while (jobs.hasNext() && budget > 0 && assembled < 12) {
			var job = jobs.next();
			if (assembly.expired(job, now)) { jobs.remove(); dirty.run(); continue; }
			if (!assembly.ready(job)) continue;
			jobs.remove(); budget--; quota.consume(1); dirty.run();
			if (assembly.placeIfValid(job)) assembled++;
		}
	}
	private TerrainLeaseScheduler() {}
}
