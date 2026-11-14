package com.example.examplemod.magic;
import com.example.examplemod.infrastructure.fabric.magic.AbsoluteZeroAdapter;
import com.example.examplemod.magic.spell.water.absolute_zero.AbsoluteZeroRules;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.*;
public final class AbsoluteZero {
	public static boolean contains(Vec3 p){return AbsoluteZeroRules.contains(p.x,p.y,p.z);}
	public static boolean cast(ServerPlayer p,MagicAim aim){return AbsoluteZeroAdapter.cast(p,aim);}
	public static void tick(MagicAreaEntity a){AbsoluteZeroAdapter.tick(a);}
	public static void save(MagicAreaEntity a,ValueOutput out){AbsoluteZeroAdapter.save(a,out);}
	public static void load(MagicAreaEntity a,ValueInput in){AbsoluteZeroAdapter.load(a,in);}
	private AbsoluteZero() { }
}
