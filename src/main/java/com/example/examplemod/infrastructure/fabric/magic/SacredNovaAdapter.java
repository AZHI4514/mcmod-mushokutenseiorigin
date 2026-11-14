package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.MagicAim;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record SacredNovaAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return HolySanctuaryAdapter.cast(player,new MagicAim(yaw,pitch));}
}
