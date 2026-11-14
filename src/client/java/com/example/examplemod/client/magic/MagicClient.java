package com.example.examplemod.client.magic;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.ModItems;
import com.example.examplemod.magic.MagicAttachments;
import com.example.examplemod.magic.MagicState;

public final class MagicClient {
	public static final KeyMapping KNOWLEDGE=MagicKeyBindings.KNOWLEDGE,WHEEL=MagicKeyBindings.WHEEL,CAST=MagicKeyBindings.CAST;
	public static final KeyMapping[] CHANT_KEYS=MagicKeyBindings.CHANT_KEYS;
	public static void initialize() {
		SpellSpriteParticle.initialize();
		SpellVolumeEffects.initialize();IllusionProjection.initialize();
		MagicClientNetworking.initialize();
		MagicRendererRegistration.initialize();
		HudElementRegistry.addLast(ExampleMod.id("magic_hud"), (graphics, delta) -> MagicHud.drawHud(graphics));
		UseItemCallback.EVENT.register((player, level, hand) -> {
			if (level.isClientSide() && player == Minecraft.getInstance().player
					&& com.example.examplemod.magic.MagicBooks.isTextbook(player.getItemInHand(hand))) {
				interrupt();
				Minecraft.getInstance().setScreen(new MagicBookScreen(player.getItemInHand(hand)));
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		});
		ClientTickEvents.START_CLIENT_TICK.register(MagicInputController::updateWheelInput);
		ClientTickEvents.END_CLIENT_TICK.register(MagicClient::tick);
	}

	public static MagicState state() {
		var player = Minecraft.getInstance().player;
		return player == null ? MagicState.initial(100) : player.getAttachedOrElse(MagicAttachments.STATE, MagicState.initial(100));
	}

	public static void send(String action,String spell) { MagicClientNetworking.send(action,spell); }

	public static void interrupt(){MagicInputController.interrupt();}
	public static boolean physicallyDown(KeyMapping mapping){return MagicInputController.physicallyDown(mapping);}
	static KeyMapping[] movementKeys(Minecraft client){return MagicInputController.movementKeys(client);}
	private static void tick(Minecraft client){MagicHandGlow.tick(client);SpellVisualAnimations.tick(client);MagicInputController.tick(client);}

	private MagicClient() {
	}
}
