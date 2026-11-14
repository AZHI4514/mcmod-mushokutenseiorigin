package com.example.examplemod.magic.spell.special.divination_magic;
import java.util.function.IntPredicate;
public final class DivinationRules {
	public static int firstIntersection(IntPredicate intersects){for(int tick=1;tick<=200;tick++)if(intersects.test(tick))return tick;return 0;}
	public static double reportedTime(double earliest,double random){return Math.clamp(earliest+(random*4-2),0,12);}
	private DivinationRules() { }
}
