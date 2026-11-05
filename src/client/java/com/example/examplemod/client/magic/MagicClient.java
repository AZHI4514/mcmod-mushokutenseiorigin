package com.example.examplemod.client.magic;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.ModItems;
import com.example.examplemod.magic.MagicActionPayload;
import com.example.examplemod.magic.MagicAttachments;
import com.example.examplemod.magic.MagicState;
import com.example.examplemod.magic.MagicSystem;

public final class MagicClient {
	private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(ExampleMod.id("magic"));
	public static final KeyMapping KNOWLEDGE = key("knowledge", GLFW.GLFW_KEY_G);
	public static final KeyMapping WHEEL = key("wheel", GLFW.GLFW_KEY_R);
	public static final KeyMapping CAST = key("cast", GLFW.GLFW_KEY_V);
	private static boolean mustRelease = true;
	private static boolean armed;
	private static int sequence;
	private static int localDelay;
	private static net.minecraft.client.player.LocalPlayer lastPlayer;

	private static KeyMapping key(String name, int code) {
		return KeyMappingHelper.registerKeyMapping(new KeyMapping("key.mushokutenseiorigin." + name,
				InputConstants.Type.KEYSYM, code, CATEGORY));
	}

	public static void initialize() {
		EntityRendererRegistry.register(MagicSystem.WATER_BALL, WaterBallRenderer::new);
		HudElementRegistry.addLast(ExampleMod.id("magic_hud"), (graphics, delta) -> MagicScreens.drawHud(graphics));
		UseItemCallback.EVENT.register((player, level, hand) -> {
			if (level.isClientSide() && player == Minecraft.getInstance().player
					&& player.getItemInHand(hand).is(ModItems.ZENITH_GREYRAT_MAGIC_BOOK)) {
				interrupt();
				Minecraft.getInstance().setScreen(new MagicBookScreen());
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		});
		ClientTickEvents.END_CLIENT_TICK.register(MagicClient::tick);
	}

	public static MagicState state() {
		var player = Minecraft.getInstance().player;
		return player == null ? MagicState.initial(100) : player.getAttachedOrElse(MagicAttachments.STATE, MagicState.initial(100));
	}

	public static void send(String action, String spell) {
		if (ClientPlayNetworking.canSend(MagicActionPayload.TYPE))
			ClientPlayNetworking.send(new MagicActionPayload(action, spell, ++sequence));
	}

	public static void interrupt() {
		if (armed) send("stop", "");
		armed = false;
		mustRelease = true;
		localDelay = 0;
	}

	/** 直接读取当前绑定的物理按键，Screen 打开后 mapping.isDown 会被原版清空。 */
	public static boolean physicallyDown(KeyMapping mapping) {
		if (mapping.isUnbound()) return false;
		var key = KeyMappingHelper.getBoundKeyOf(mapping);
		var window = Minecraft.getInstance().getWindow();
		if (key.getType() == InputConstants.Type.MOUSE)
			return GLFW.glfwGetMouseButton(window.handle(), key.getValue()) == GLFW.GLFW_PRESS;
		if (key.getType() == InputConstants.Type.SCANCODE) {
			for (int code = GLFW.GLFW_KEY_SPACE; code <= GLFW.GLFW_KEY_LAST; code++)
				if (GLFW.glfwGetKeyScancode(code) == key.getValue()) return InputConstants.isKeyDown(window, code);
			return false;
		}
		return InputConstants.isKeyDown(window, key.getValue());
	}

	private static void tick(Minecraft client) {
		if (client.player != lastPlayer) { interrupt(); lastPlayer = client.player; }
		if (client.player == null || client.level == null) { armed = false; mustRelease = true; return; }
		if (!client.isWindowActive() || client.screen != null || client.player.isUsingItem()
				|| !client.player.isAlive() || client.player.isSpectator()) {
			interrupt();
			while (KNOWLEDGE.consumeClick()) { }
			while (WHEEL.consumeClick()) { }
			return;
		}
		if (KNOWLEDGE.consumeClick()) {
			interrupt();
			client.setScreen(new MagicScreens.Knowledge(false));
			return;
		}
		if (WHEEL.consumeClick()) {
			interrupt();
			client.setScreen(new MagicScreens.Wheel());
			return;
		}
		boolean down = physicallyDown(CAST);
		if (!down) {
			if (armed) send("release", "");
			armed = false;
			mustRelease = false;
			localDelay = 0;
			return;
		}
		if (mustRelease) return;
		if (!armed) { send("arm", ""); armed = true; }
		if (localDelay > 0) localDelay--;
		if (localDelay == 0 && !state().selected().isEmpty()) {
			send("cast", state().selected());
			localDelay = 2;
		}
	}

	private MagicClient() {
	}
}
