package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.infrastructure.fabric.magic.AreaRuntimeAdapter;
import com.example.examplemod.magic.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record WaterShieldAdapter(ServerPlayer player) implements SelfEffect {
	public boolean apply(float yaw,float pitch){return AreaRuntimeAdapter.cast(player,SpellDefinition.WATER_SHIELD,new MagicAim(yaw,pitch));}
}
