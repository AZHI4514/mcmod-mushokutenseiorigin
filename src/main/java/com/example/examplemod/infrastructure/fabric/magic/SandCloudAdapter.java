package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record SandCloudAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return SmokeFieldRuntimeAdapter.cast(player,SpellDefinition.SAND_CLOUD,new MagicAim(yaw,pitch));}
}
