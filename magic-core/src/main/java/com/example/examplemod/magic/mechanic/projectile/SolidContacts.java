package com.example.examplemod.magic.mechanic.projectile;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/** Per-sweep solid contacts; equal-distance shapes keep the first impact. */
public final class SolidContacts<B,I> {
	private final Map<B,Double> contacts=new HashMap<>();
	private double wall=Double.POSITIVE_INFINITY;
	private B block;
	private I impact;
	public void accept(B candidate,double contact,double distance,Supplier<I> point) {
		if(contact<=distance) contacts.merge(candidate,contact,Math::min);
		if(contact<wall) {
			wall=contact;
			block=candidate;
			impact=point.get();
		}
	}
	public double wall() {return wall;}
	public B block() {return block;}
	public I impact() {return impact;}
	public Map<B,Double> contacts() {return contacts;}
}
