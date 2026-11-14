package com.example.examplemod.magic;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.examplemod.magic.domain.geometry.BladeSectionVolume;
import com.example.examplemod.magic.domain.model.*;
import com.example.examplemod.magic.testing.FrozenBladeSection;
class BladeSectionMigrationTest {
	@Test void frozenPreMigrationGeometryMatchesSeededDirectionsAndObstacleUnions() {
		var random=new Random(260111);
		for(int sample=0;sample<500;sample++) {
			float yaw=random.nextFloat()*360-180;double pitch=random.nextDouble()*Math.PI-Math.PI/2,angle=Math.toRadians(yaw);
			var direction=new Vec3d(-Math.sin(angle)*Math.cos(pitch),-Math.sin(pitch),Math.cos(angle)*Math.cos(pitch));
			var from=new Vec3d(random.nextDouble()*4,random.nextDouble()*4,random.nextDouble()*4);double distance=random.nextDouble()*3;
			var old=new FrozenBladeSection(direction,-yaw);var moved=new BladeSectionVolume(direction,-yaw);
			var blocks=new ArrayList<Box3d>();for(int i=0;i<3;i++){double x=from.x+random.nextDouble()*4-2,y=from.y+random.nextDouble()*2-1,z=from.z+random.nextDouble()*4-2;blocks.add(new Box3d(x,y,z,x+1,y+1,z+1));}
			assertEquals(old.fullyBlocked(from,blocks),moved.fullyBlocked(from,blocks));assertEquals(old.blockingDistance(from,distance,blocks),moved.blockingDistance(from,distance,blocks));
			for(var box:blocks)assertEquals(old.contact(box,from,distance),moved.contact(box,from,distance));
		}
	}
	@Test void partialObstructionAndTinyGapPassButAdjacentUnionStops() {
		var volume=new BladeSectionVolume(new Vec3d(0,0,1),0);var from=new Vec3d(0,1,0);
		var left=new Box3d(-1.5,.5,-.5,0,1.5,.5);var right=new Box3d(0,.5,-.5,1.5,1.5,.5);
		assertFalse(volume.fullyBlocked(from,List.of(left)));assertTrue(volume.fullyBlocked(from,List.of(left,right)));
		assertFalse(volume.fullyBlocked(from,List.of(left,new Box3d(.01,.5,-.5,1.5,1.5,.5))));
		assertEquals(Double.POSITIVE_INFINITY,volume.blockingDistance(from,3,List.of(left)));
	}
	@Test void negativeYawObliqueSectionRetainsPartialPassAndFullStop() {
		float yaw=37;double a=Math.toRadians(yaw);var direction=new Vec3d(-Math.sin(a),0,Math.cos(a));
		var volume=new BladeSectionVolume(direction,-yaw);var frozen=new FrozenBladeSection(direction,-yaw);var center=new Vec3d(0,1,0);
		var partial=List.of(new Box3d(-.1,.5,-.1,.1,1.5,.1));var full=List.of(new Box3d(-3,0,-3,3,2,3));
		assertFalse(volume.fullyBlocked(center,partial));assertTrue(volume.fullyBlocked(center,full));
		assertEquals(frozen.blockingDistance(center,3,partial),volume.blockingDistance(center,3,partial));
		assertEquals(0,volume.blockingDistance(center,3,full));
	}
}
