package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record LamplightSpiritsAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return SummonRuntimeAdapter.cast(player,SpellDefinition.LAMPLIGHT_SPIRITS,new MagicAim(yaw,pitch));}
}
