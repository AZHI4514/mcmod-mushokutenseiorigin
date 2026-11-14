package com.example.examplemod.magic;
import com.example.examplemod.infrastructure.fabric.magic.MagmaGushAdapter;
import com.example.examplemod.magic.spell.fire.magma_gush.MagmaGushRules;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.*;
public final class MagmaGush {
	public static float damageAt(long age){return MagmaGushRules.damage(age);}
	public static boolean contains(Vec3 offset){return MagmaGushRules.contains(offset.x,offset.y,offset.z);}
	public static boolean cast(ServerPlayer p,MagicAim aim){return MagmaGushAdapter.cast(p,aim);}
	public static void tick(MagicAreaEntity area){MagmaGushAdapter.tick(area);}
	public static void save(MagicAreaEntity area,ValueOutput out){MagmaGushAdapter.save(area,out);}
	public static void load(MagicAreaEntity area,ValueInput in){MagmaGushAdapter.load(area,in);}
	private MagmaGush() { }
}
