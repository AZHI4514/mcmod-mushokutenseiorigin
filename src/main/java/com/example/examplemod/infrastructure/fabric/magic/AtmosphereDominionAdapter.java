package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record AtmosphereDominionAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return MaintainedFieldRuntimeAdapter.cast(player,SpellDefinition.ATMOSPHERE_DOMINION,new MagicAim(yaw,pitch));}
}
