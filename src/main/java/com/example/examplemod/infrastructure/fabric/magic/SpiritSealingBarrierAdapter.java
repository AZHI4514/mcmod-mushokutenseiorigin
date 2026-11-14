package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record SpiritSealingBarrierAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return FiniteBarrierRuntimeAdapter.cast(player,SpellDefinition.SPIRIT_SEALING_BARRIER,new MagicAim(yaw,pitch));}
}
