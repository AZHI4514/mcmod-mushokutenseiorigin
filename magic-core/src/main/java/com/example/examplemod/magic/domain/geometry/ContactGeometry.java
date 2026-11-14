package com.example.examplemod.magic.domain.geometry;
import com.example.examplemod.magic.domain.model.*;
/** Contact predicates over immutable core values; no game collision API is required. */
public final class ContactGeometry {
	public static boolean body(Box3d owner,Box3d target){return owner.inflate(.12).intersects(target);}
	public static boolean ring(Box3d box,Vec3d center,double radius){
		if(box.maxY<center.y||box.minY>center.y+.65)return false;
		double nearX=Math.clamp(center.x,box.minX,box.maxX)-center.x,nearZ=Math.clamp(center.z,box.minZ,box.maxZ)-center.z;
		double farX=Math.max(Math.abs(box.minX-center.x),Math.abs(box.maxX-center.x)),farZ=Math.max(Math.abs(box.minZ-center.z),Math.abs(box.maxZ-center.z));
		double inner=Math.max(0,radius-.15),outer=radius+.15;
		return nearX*nearX+nearZ*nearZ<=outer*outer&&farX*farX+farZ*farZ>=inner*inner;
	}
	private ContactGeometry(){}
}
