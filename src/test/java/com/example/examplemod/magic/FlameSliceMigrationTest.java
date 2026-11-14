package com.example.examplemod.magic;
import java.util.List;
import java.util.Random;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
/** Frozen source differential, including clipping order and exact tolerances. No simulated game world. */
public final class FlameSliceMigrationTest {
	private static void equal(double old,double value) {
		if(Double.doubleToLongBits(old)!=Double.doubleToLongBits(value) && (!Double.isFinite(old)||!Double.isFinite(value)||Math.abs(old-value)>1E-10))throw new AssertionError("Flame geometry drift: "+old+" vs "+value);
	}
	public static void main(String[] args) {
		var random=new Random(111107);
		for(int i=0;i<1000;i++) {
			float yaw=random.nextFloat()*720-360;float pitch=random.nextFloat()*180-90;
			var direction=Vec3.directionFromRotation(pitch,yaw);if(i%50==0)direction=Vec3.ZERO;
			var old=new FrozenFlameSliceVolume(direction,yaw);var value=new FlameSliceVolume(direction,yaw);
			var from=new Vec3(random.nextDouble(-5,5),random.nextDouble(-5,5),random.nextDouble(-5,5));
			var a=new AABB(from.x+random.nextDouble(-3,3),from.y+random.nextDouble(-2,2),from.z+random.nextDouble(-3,3),from.x+random.nextDouble(-3,3),from.y+random.nextDouble(-2,2),from.z+random.nextDouble(-3,3));
			var large=new AABB(from.x-4,from.y-4,from.z-4,from.x+4,from.y+4,from.z+4);
			var boxes=i%3==0?List.of(a,large):List.of(a);double distance=random.nextDouble(0,3);
			equal(old.contact(a,from,distance),value.contact(a,from,distance));
			if(old.fullyBlocked(from,boxes)!=value.fullyBlocked(from,boxes))throw new AssertionError("Cross-section drift");
			equal(old.blockingDistance(from,distance,boxes),value.blockingDistance(from,distance,boxes));
			var x=old.bounds(from);var y=value.bounds(from);equal(x.minX,y.minX);equal(x.minY,y.minY);equal(x.minZ,y.minZ);equal(x.maxX,y.maxX);equal(x.maxY,y.maxY);equal(x.maxZ,y.maxZ);
		}
		System.out.println("Flame slice: 1,000 frozen-old/new swept contacts, section unions, blocking distances and bounds matched.");
	}
}
