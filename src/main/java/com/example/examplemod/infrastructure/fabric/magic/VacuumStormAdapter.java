package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record VacuumStormAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return WindFieldRuntimeAdapter.cast(player,SpellDefinition.VACUUM_STORM,new MagicAim(yaw,pitch));}
}
