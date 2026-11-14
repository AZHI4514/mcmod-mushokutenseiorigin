package com.example.examplemod.magic.mechanic.projectile;
/** Vanilla airborne drag/gravity solver; no game runtime or vector dependency. */
public final class LaunchBallistics {
	public static double launchPeak(double velocity) {
		double height=0;for(int tick=0;tick<200&&velocity>0;tick++){height+=velocity;velocity=(velocity-0.08)*0.98;}return height;
	}
	public static double verticalLaunch(double height) {
		double low=0,high=Math.max(2,height);for(int i=0;i<48;i++){double middle=(low+high)/2;if(launchPeak(middle)<height)low=middle;else high=middle;}return (low+high)/2;
	}
	private LaunchBallistics() {}
}
