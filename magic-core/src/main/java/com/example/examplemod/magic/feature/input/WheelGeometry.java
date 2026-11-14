package com.example.examplemod.magic.feature.input;
/** Pure geometry shared by sector hit-testing and rendering. */
public final class WheelGeometry {
	public static int radius(int width,int height) {
		return Math.max(1,(int)Math.min(Math.sqrt((double)width*height/(9*Math.PI)),Math.min(width,height)/2.0-4));
	}
	public static int hitSlot(int width,int height,int count,double mx,double my) {
		double dx=mx-width/2,dy=my-height/2;
		double distance=dx*dx+dy*dy;
		if(count==0||distance<100||distance>(double)radius(width,height)*radius(width,height))return -1;
		double step=Math.PI*2/count;
		double angle=(Math.atan2(dy,dx)+Math.PI/2+step/2+Math.PI*2)%(Math.PI*2);
		return Math.min(count-1,(int)(angle/step));
	}
	private WheelGeometry() {}
}
