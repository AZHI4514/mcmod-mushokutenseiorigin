package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record GravityMagicAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return MaintainedFieldRuntimeAdapter.cast(player,SpellDefinition.GRAVITY_MAGIC,new MagicAim(yaw,pitch));}
}
