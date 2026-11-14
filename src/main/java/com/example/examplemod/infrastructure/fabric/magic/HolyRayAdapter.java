package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record HolyRayAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return HolyInstantAdapter.cast(player,SpellDefinition.HOLY_RAY,new MagicAim(yaw,pitch));}
}
