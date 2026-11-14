package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record ViolentStormAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return WindFieldRuntimeAdapter.cast(player,SpellDefinition.VIOLENT_STORM,new MagicAim(yaw,pitch));}
}
