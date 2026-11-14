package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record FlameTornadoAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return WindFieldRuntimeAdapter.cast(player,SpellDefinition.FLAME_TORNADO,new MagicAim(yaw,pitch));}
}
