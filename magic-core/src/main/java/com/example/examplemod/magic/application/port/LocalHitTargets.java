package com.example.examplemod.magic.application.port;
import com.example.examplemod.magic.domain.model.Box3d;
/** Ordered world candidates; visibility and damage stay at the world boundary. */
public interface LocalHitTargets<T> {
	Iterable<T> candidates();
	Box3d bounds(T target);
	boolean visible(T target);
	void damage(T target);
}
