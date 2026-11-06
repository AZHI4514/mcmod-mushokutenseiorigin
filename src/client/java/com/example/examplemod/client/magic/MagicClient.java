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
	private static boolean wheelMustRelease;
	private static boolean suppressAttack;
	private static boolean suppressUse;
	private static int sequence;
	private static int localDelay;
	private static net.minecraft.client.player.LocalPlayer lastPlayer;

	private static KeyMapping key(String name, int code) {
		return KeyMappingHelper.registerKeyMapping(new KeyMapping("key.mushokutenseiorigin." + name,
				InputConstants.Type.KEYSYM, code, CATEGORY));
	}

	public static void initialize() {
		EntityRendererRegistry.register(MagicSystem.WATER_BALL, context -> new WaterBallRenderer(context, "water_ball"));
		EntityRendererRegistry.register(MagicSystem.FIREBALL, context -> new WaterBallRenderer(context, "fireball"));
		EntityRendererRegistry.register(MagicSystem.WIND_BALL, context -> new WaterBallRenderer(context, "wind_ball"));
		EntityRendererRegistry.register(MagicSystem.ROCK_BULLET, context -> new WaterBallRenderer(context, "rock_bullet"));
		EntityRendererRegistry.register(MagicSystem.WATER_ARROW, context -> new WaterBallRenderer(context, "water_arrow"));
		EntityRendererRegistry.register(MagicSystem.ICE_SMASH, context -> new WaterBallRenderer(context, "ice_smash"));
		EntityRendererRegistry.register(MagicSystem.FLAME_SLICE, FlameSliceRenderer::new);
		HudElementRegistry.addLast(ExampleMod.id("magic_hud"), (graphics, delta) -> MagicScreens.drawHud(graphics));
		UseItemCallback.EVENT.register((player, level, hand) -> {
			if (level.isClientSide() && player == Minecraft.getInstance().player
					&& com.example.examplemod.magic.MagicBooks.isTextbook(player.getItemInHand(hand))) {
				interrupt();
				Minecraft.getInstance().setScreen(new MagicBookScreen());
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		});
		ClientTickEvents.START_CLIENT_TICK.register(MagicClient::updateWheelInput);
		ClientTickEvents.END_CLIENT_TICK.register(MagicClient::tick);
	}

	public static MagicState state() {
		var player = Minecraft.getInstance().player;
		return player == null ? MagicState.initial(100) : player.getAttachedOrElse(MagicAttachments.STATE, MagicState.initial(100));
	}

	public static void send(String action, String spell) {
		var player = Minecraft.getInstance().player;
		if (player != null && ClientPlayNetworking.canSend(MagicActionPayload.TYPE))
			ClientPlayNetworking.send(new MagicActionPayload(action, spell, ++sequence, player.getYRot(), player.getXRot()));
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

	/** 只透传移动键；鼠标按键保持隔离，直到关闭后物理松开，防止误攻击。 */
	private static void updateWheelInput(Minecraft client) {
		boolean wheel = client.screen instanceof MagicScreens.Wheel;
		if (wheel) {
			for (var key : movementKeys(client)) key.setDown(client.isWindowActive() && physicallyDown(key));
			suppressAttack = true;
			suppressUse = true;
		}
		if (suppressAttack) suppressAttack = suppressMouse(client.options.keyAttack, wheel);
		if (suppressUse) suppressUse = suppressMouse(client.options.keyUse, wheel);
	}

	private static boolean suppressMouse(KeyMapping key, boolean wheel) {
		key.setDown(false);
		while (key.consumeClick()) { }
		return wheel || physicallyDown(key);
	}

	static KeyMapping[] movementKeys(Minecraft client) {
		var options = client.options;
		return new KeyMapping[]{options.keyUp, options.keyDown, options.keyLeft, options.keyRight,
				options.keyJump, options.keyShift, options.keySprint};
	}

	private static void tick(Minecraft client) {
		if (client.player != lastPlayer) { interrupt(); lastPlayer = client.player; }
		if (client.player == null || client.level == null) { armed = false; mustRelease = true; return; }
		if (!physicallyDown(WHEEL)) wheelMustRelease = false;
		if (!client.isWindowActive() || client.screen != null || client.player.isUsingItem()
				|| !client.player.isAlive() || client.player.isSpectator()) {
			interrupt();
			if (!(client.screen instanceof MagicScreens.Wheel) && physicallyDown(WHEEL)) wheelMustRelease = true;
			while (KNOWLEDGE.consumeClick()) { }
			while (WHEEL.consumeClick()) { }
			return;
		}
		if (KNOWLEDGE.consumeClick()) {
			interrupt();
			client.setScreen(new MagicScreens.Knowledge(false));
			return;
		}
		if (WHEEL.consumeClick() && !wheelMustRelease && physicallyDown(WHEEL)) {
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
