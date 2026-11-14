package com.example.examplemod.magic;
import com.example.examplemod.infrastructure.fabric.magic.FlashOverAdapter;
import com.example.examplemod.magic.spell.fire.flash_over.FlashOverRules;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.*;
public final class FlashOver {
	public static float residual(long age){return FlashOverRules.residual(age);}
	public static boolean contains(Vec3 p){return FlashOverRules.contains(p.x,p.y,p.z);}
	public static boolean cast(ServerPlayer p,MagicAim aim){return FlashOverAdapter.cast(p,aim);}
	public static void tick(MagicAreaEntity a){FlashOverAdapter.tick(a);}
	public static void shorten(MagicAreaEntity a,int ticks){FlashOverAdapter.shorten(a,ticks);}
	public static void save(MagicAreaEntity a,ValueOutput out){FlashOverAdapter.save(a,out);}
	public static void load(MagicAreaEntity a,ValueInput in){FlashOverAdapter.load(a,in);}
	private FlashOver() { }
}
