package com.example.examplemod.magic;
import com.example.examplemod.infrastructure.fabric.magic.QuagmireAdapter;
import com.example.examplemod.magic.spell.mixed.quagmire.QuagmireRules;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.*;
public final class Quagmire {
	public static boolean contains(Vec3 offset){return QuagmireRules.contains(offset.x,offset.y,offset.z);}
	public static double jumpVelocity(double upward){return QuagmireRules.jumpVelocity(upward);}
	public static boolean cast(ServerPlayer player,MagicAim aim){return QuagmireAdapter.cast(player,aim);}
	public static void tick(MagicAreaEntity area){QuagmireAdapter.tick(area);}
	public static void save(MagicAreaEntity area,ValueOutput out){QuagmireAdapter.save(area,out);}
	public static void load(MagicAreaEntity area,ValueInput in){QuagmireAdapter.load(area,in);}
	private Quagmire() { }
}
