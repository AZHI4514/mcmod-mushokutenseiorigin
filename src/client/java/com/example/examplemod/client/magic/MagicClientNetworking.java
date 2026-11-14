package com.example.examplemod.client.magic;
import net.minecraft.client.Minecraft;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.example.examplemod.magic.MagicActionPayload;
/** Client transport, including the monotonically increasing request sequence. */
final class MagicClientNetworking {
	private static int sequence;
	static void initialize() {
		ClientPlayNetworking.registerGlobalReceiver(com.example.examplemod.magic.SpellVisualPayload.TYPE, (packet, context) -> SpellVisualAnimations.receive(packet));
		ClientPlayNetworking.registerGlobalReceiver(com.example.examplemod.magic.ChantPayload.TYPE, (packet, context) -> {
			if (Minecraft.getInstance().screen instanceof MagicBookScreen book) book.receive(packet);
		});
	}
	static void send(String action,String spell) {
		var player=Minecraft.getInstance().player;
		if(player!=null&&ClientPlayNetworking.canSend(MagicActionPayload.TYPE))
			ClientPlayNetworking.send(new MagicActionPayload(action,spell,++sequence,player.getYRot(),player.getXRot()));
	}
	private MagicClientNetworking() {}
}
