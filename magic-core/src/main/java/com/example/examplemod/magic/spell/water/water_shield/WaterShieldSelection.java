package com.example.examplemod.magic.spell.water.water_shield;
import java.util.function.*;
public final class WaterShieldSelection {
	public record Contact<T>(T shield,double distance) { }
	public static <T> Contact<T> nearest(Iterable<T> shields,Predicate<T> eligible,ToDoubleFunction<T> contact){
		double first=Double.POSITIVE_INFINITY;T selected=null;
		for(T shield:shields){if(!eligible.test(shield))continue;double distance=contact.applyAsDouble(shield);if(distance<first){first=distance;selected=shield;}}
		return selected==null?null:new Contact<>(selected,first);
	}
	private WaterShieldSelection() { }
}
