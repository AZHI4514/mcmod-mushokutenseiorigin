package com.example.examplemod.magic.spell.earth.earth_hedgehog;
public final class EarthHedgehogRules {
	public record Offset(double x,double z) { }
	public static Offset offset(int index){double a=index*2.3999632297,r=1+4.4*Math.sqrt((index+.5)/12);return new Offset(Math.cos(a)*r,Math.sin(a)*r);}
	public static boolean active(long age,int index){return age>=index*2L&&age<400;}
	public static boolean erupt(long age,int index){return age==index*2L;}
	public static boolean mayHit(int count){return count>=0&&count<2;}
	private EarthHedgehogRules() { }
}
