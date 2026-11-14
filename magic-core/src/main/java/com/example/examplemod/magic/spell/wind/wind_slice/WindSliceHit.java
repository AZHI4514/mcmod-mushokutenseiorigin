package com.example.examplemod.magic.spell.wind.wind_slice;
import java.util.UUID;
import com.example.examplemod.magic.mechanic.projectile.ImpactHistory;
/** Coordinates the existing per-contact burst; direct victim was claimed by flight before entry. */
public final class WindSliceHit {
	public interface Targets<T> {
		Iterable<T> candidates();
		UUID identity(T target);
		boolean direct(T target);
		void damage(T target);
	}
	public static boolean inRadius(double squaredDistance) { return squaredDistance<=WindSliceSpec.RADIUS*WindSliceSpec.RADIUS; }
	public static <T> void apply(ImpactHistory history,Targets<T> targets) {
		for(var target:targets.candidates()) {
			if(targets.direct(target)||history.claim(targets.identity(target)))targets.damage(target);
		}
	}
	private WindSliceHit() { }
}
