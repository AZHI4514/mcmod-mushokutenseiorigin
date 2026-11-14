package com.example.examplemod.magic.spell.wind.wind_blast;
import com.example.examplemod.magic.domain.model.Vec3d;
/** One launch impulse; subsequent movement belongs to vanilla physics. */
public final class WindBlastRules {
	public static final int MANA=8,SLOW_FALL_TICKS=100;
	public static final double DISTANCE=10;
	public static Vec3d velocity(Vec3d direction) {
		var n=direction.normalize();
		double horizontal=DISTANCE*(1-.91);
		double y=n.y>0?verticalLaunch(DISTANCE*n.y):n.y<0?-1.2*Math.abs(n.y):.42;
		return new Vec3d(n.x*horizontal,y,n.z*horizontal);
	}
	private static double peak(double velocity) {
		double height=0;for(int tick=0;tick<200&&velocity>0;tick++){height+=velocity;velocity=(velocity-0.08)*0.98;}return height;
	}
	private static double verticalLaunch(double height) {
		double low=0,high=Math.max(2,height);for(int i=0;i<48;i++){double middle=(low+high)/2;if(peak(middle)<height)low=middle;else high=middle;}return (low+high)/2;
	}
	private WindBlastRules() { }
}
