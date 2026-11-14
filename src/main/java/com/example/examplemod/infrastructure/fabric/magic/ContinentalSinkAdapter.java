package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record ContinentalSinkAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return GroundFieldRuntimeAdapter.cast(player,SpellDefinition.CONTINENTAL_SINK,new MagicAim(yaw,pitch));}
}
