package com.example.examplemod.magic.mechanic.terrain;
import java.util.*;
/** Ordered pending construction, not a fixed one-block-per-tick animation. */
public final class ColumnGrowth<T> {
	public interface Placement<T> {
		boolean available(T cell);
		boolean occupied(T cell);
		void place(T cell,int remainingTicks);
	}
	private final List<T> pending;
	private final long expiry;
	public ColumnGrowth(List<T> cells,long expiry) { pending=new ArrayList<>(cells);this.expiry=expiry; }
	/** True removes this pending job; placed block leases remain independent. */
	public boolean tick(long now,Placement<T> world) {
		if(now>=expiry)return true;
		var it=pending.iterator();
		while(it.hasNext()) {
			var cell=it.next();if(!world.available(cell)){it.remove();continue;}
			if(world.occupied(cell))break;
			world.place(cell,(int)(expiry-now));it.remove();
		}
		return pending.isEmpty();
	}
}
