package com.example.examplemod.magic.mechanic.field;
public final class WindFieldRules {
	public static boolean contains(double x,double y,double z,double radius,double height){return y>=0&&y<=height&&x*x+z*z<=radius*radius;}
	public record Velocity(double x,double y,double z) { }
	public static Velocity impulse(double x,double z,double vx,double vy,double vz,boolean tornado,boolean tempest,boolean player){double length=Math.sqrt(x*x+z*z);double nx=length<1.0E-5?0:x/length,nz=length<1.0E-5?0:z/length;double ax=tornado?nx*-.12:tempest?-nz*.08:nx*.2,ay=tornado?.08:tempest?.04:0,az=tornado?nz*-.12:tempest?nx*.08:nz*.2;double y=vy+ay;return new Velocity(vx+ax,player&&(tornado||tempest)?Math.min(.35,y):y,vz+az);}
	private WindFieldRules() { }
}
