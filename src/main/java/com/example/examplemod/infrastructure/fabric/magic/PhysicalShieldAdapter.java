package com.example.examplemod.infrastructure.fabric.magic;
import net.minecraft.server.level.ServerPlayer;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
public final class PhysicalShieldAdapter implements SelfEffect {
	private final ServerPlayer player;
	public PhysicalShieldAdapter(ServerPlayer player){this.player=player;}
	public boolean apply(float yaw,float pitch){return PhysicalShieldRuntimeAdapter.release(player,new MagicAim(yaw,pitch));}
}
