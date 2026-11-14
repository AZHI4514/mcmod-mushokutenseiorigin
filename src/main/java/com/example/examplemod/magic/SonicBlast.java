package com.example.examplemod.magic;

import com.example.examplemod.infrastructure.fabric.magic.SonicBlastAdapter;
import com.example.examplemod.magic.spell.wind.sonic_blast.SonicBlastRules;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

/** Compatibility entry points; no separate payment or world implementation. */
public final class SonicBlast {
	public static boolean contains(Vec3 offset,Vec3 direction) {
		return SonicBlastRules.contains(offset.x,offset.y,offset.z,direction.x,direction.y,direction.z);
	}
	public static boolean cast(ServerPlayer owner,MagicAim aim) {return SonicBlastAdapter.cast(owner,aim);}
	private SonicBlast() { }
}
