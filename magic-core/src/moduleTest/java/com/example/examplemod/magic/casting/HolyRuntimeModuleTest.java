package com.example.examplemod.magic.casting;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.mechanic.targeting.*;
class HolyRuntimeModuleTest {
	@Test void columnIncludesAxisEndpointsAndTwoBlockRadius(){assertTrue(HolyGeometry.column(2,0,0,0,0,1,10));assertTrue(HolyGeometry.column(0,0,10,0,0,1,10));assertFalse(HolyGeometry.column(2.001,0,0,0,0,1,10));assertFalse(HolyGeometry.column(0,0,10.001,0,0,1,10));assertFalse(HolyGeometry.column(0,0,-.01,0,0,1,10));}
	@Test void oldProjectionMatchesRandomDirections(){var r=new Random(741);for(int i=0;i<10000;i++){double x=r.nextDouble()*20-10,y=r.nextDouble()*20-10,z=r.nextDouble()*20-10,ax=r.nextDouble(),ay=r.nextDouble(),az=r.nextDouble();double n=Math.sqrt(ax*ax+ay*ay+az*az);ax/=n;ay/=n;az/=n;double dot=x*ax+y*ay+z*az,dx=x-ax*dot,dy=y-ay*dot,dz=z-az*dot;assertEquals(dot>=0&&dot<=10&&dx*dx+dy*dy+dz*dz<=4,HolyGeometry.column(x,y,z,ax,ay,az,10));}}
	@Test void eligibilityShortCircuitsPlayersDeadOwnersBeforeTagRead(){for(int reject=0;reject<5;reject++){var calls=new ArrayList<String>();final int f=reject;var t=new HolyEligibility.Target(){public boolean player(){calls.add("player");return f==0;}public boolean alive(){calls.add("alive");return f!=1;}public boolean owner(){calls.add("owner");return f==2;}public boolean tagged(){calls.add("tag");return f!=3;}};assertEquals(reject==4,HolyEligibility.eligible(t));assertEquals(List.of("player","alive","owner","tag").subList(0,Math.min(reject+1,4)),calls);}}
}
