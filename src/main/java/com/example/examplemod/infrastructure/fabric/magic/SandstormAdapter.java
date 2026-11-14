package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record SandstormAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return SmokeFieldRuntimeAdapter.cast(player,SpellDefinition.SANDSTORM,new MagicAim(yaw,pitch));}
}
