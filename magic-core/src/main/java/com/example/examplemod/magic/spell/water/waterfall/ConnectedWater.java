package com.example.examplemod.magic.spell.water.waterfall;
import java.util.*;
import java.util.function.Predicate;
/** Bounded six-neighbour search; the historical cap is checked between expanded cells. */
public final class ConnectedWater {
	public record Cell(int x,int y,int z) { }
	private static final int[][] DIRECTIONS={{0,-1,0},{0,1,0},{0,0,-1},{0,0,1},{-1,0,0},{1,0,0}};
	private ConnectedWater() { }
	public static boolean refreshDue(long gameTime) { return gameTime%10==0; }
	public static Set<Cell> find(Cell origin,Predicate<Cell> loadedWater) {
		var connected=new HashSet<Cell>();var queue=new ArrayDeque<Cell>();
		connected.add(origin);queue.add(origin);
		while(!queue.isEmpty()&&connected.size()<4096) {
			var at=queue.removeFirst();
			for(var d:DIRECTIONS) {
				var next=new Cell(at.x()+d[0],at.y()+d[1],at.z()+d[2]);
				if(Math.abs((long)next.x()-origin.x())>16||Math.abs((long)next.y()-origin.y())>16||Math.abs((long)next.z()-origin.z())>16||connected.contains(next))continue;
				if(loadedWater.test(next)) { connected.add(next);queue.addLast(next); }
			}
		}
		return Set.copyOf(connected);
	}
}
