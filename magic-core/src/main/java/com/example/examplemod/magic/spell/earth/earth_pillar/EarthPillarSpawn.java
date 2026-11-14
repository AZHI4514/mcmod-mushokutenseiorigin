package com.example.examplemod.magic.spell.earth.earth_pillar;
import com.example.examplemod.magic.domain.model.*;
/** Spawn geometry only; block grid enumeration remains at the world boundary. */
public final class EarthPillarSpawn {
 public static Vec3d top(Vec3d point){return point.add(new Vec3d(0,6,0));}
 public static Box3d bounds(Vec3d top){return new Box3d(top.x-1.5,top.y,top.z-1.5,top.x+1.5,top.y+3,top.z+1.5);}
 private EarthPillarSpawn() { }
}
