package com.example.examplemod.magic.spell.wind.wind_bind;
/** Effect application owns the success boundary; visual sync follows only a true result. */
public final class WindBindEffect {
	public interface Target {
		boolean apply(int duration);
		void showBound();
	}
	public static boolean apply(Target target) {
		if(!target.apply(WindBindSpec.DURATION))return false;
		target.showBound();return true;
	}
	private WindBindEffect() { }
}
