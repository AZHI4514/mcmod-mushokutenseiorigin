package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record NecromancyAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return SummonRuntimeAdapter.cast(player,SpellDefinition.NECROMANCY,new MagicAim(yaw,pitch));}
}
