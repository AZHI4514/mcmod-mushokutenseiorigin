package com.example.examplemod.infrastructure.fabric.magic;
import com.example.examplemod.magic.*;
import com.example.examplemod.ExampleMod;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import com.example.examplemod.magic.application.port.SelfEffect;
import net.minecraft.server.level.ServerPlayer;
public record NuclearExplosionAdapter(ServerPlayer player) implements SelfEffect {
	private static final AttachmentType<Long> NUCLEAR_READY=AttachmentRegistry.create(ExampleMod.id("nuclear_ready"),b->b.initializer(()->0L).persistent(Codec.LONG));
	static boolean ready(ServerPlayer player){return player.level().getGameTime()>=player.getAttachedOrElse(NUCLEAR_READY,0L);}
	static void markReleased(ServerPlayer player,long start){player.setAttached(NUCLEAR_READY,start+6000);}
	public boolean apply(float yaw,float pitch){return MaintainedFieldRuntimeAdapter.cast(player,SpellDefinition.NUCLEAR_EXPLOSION,new MagicAim(yaw,pitch));}
}
