package com.example.examplemod.magic.mechanic.projectile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

/** Filters first, then stably sorts with live contact reads. */
public final class ContactCandidates {
	public static <T> List<T> collect(Iterable<T> candidates,Predicate<T> accepted,ToDoubleFunction<T> contact) {
		var result=new ArrayList<T>();
		for(T candidate:candidates) if(accepted.test(candidate)) result.add(candidate);
		result.sort(Comparator.comparingDouble(contact));
		return result;
	}
	private ContactCandidates() { }
}
