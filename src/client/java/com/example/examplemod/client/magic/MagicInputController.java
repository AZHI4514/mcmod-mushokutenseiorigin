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

import static com.example.examplemod.client.magic.MagicKeyBindings.*;
import static com.example.examplemod.client.magic.MagicClient.send;
import static com.example.examplemod.client.magic.MagicClient.state;
/** Client UI/physical input boundary; cast arming and repeat timing are pure. */
final class MagicInputController {
	private static final com.example.examplemod.magic.feature.input.CastingInputState input=new com.example.examplemod.magic.feature.input.CastingInputState();
	private static boolean wheelMustRelease,suppressAttack,suppressUse;
	private static net.minecraft.client.player.LocalPlayer lastPlayer;
	public static void interrupt() {
		if(input.interrupt())send("stop", "");
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
	static void updateWheelInput(Minecraft client) {
		boolean wheel = client.screen instanceof MagicWheelScreen;
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

	static void tick(Minecraft client) {
		if (client.player != lastPlayer) { interrupt(); lastPlayer = client.player; }
		if (client.player == null || client.level == null) { input.disconnect(); return; }
		if (!physicallyDown(WHEEL)) wheelMustRelease = false;
		if (!client.isWindowActive() || client.screen != null || client.player.isUsingItem()
				|| !client.player.isAlive() || client.player.isSpectator()) {
			interrupt();
			if (!(client.screen instanceof MagicWheelScreen) && physicallyDown(WHEEL)) wheelMustRelease = true;
			while (KNOWLEDGE.consumeClick()) { }
			while (WHEEL.consumeClick()) { }
			return;
		}
		if (KNOWLEDGE.consumeClick()) {
			interrupt();
			client.setScreen(new MagicKnowledgeScreen(false));
			return;
		}
		if (WHEEL.consumeClick() && !wheelMustRelease && physicallyDown(WHEEL)) {
			interrupt();
			client.setScreen(new MagicWheelScreen());
			return;
		}
		boolean down=physicallyDown(CAST);
		boolean modeRequested=down&&!input.mustRelease()&&!input.armed()&&client.player.isShiftKeyDown()
			&&com.example.examplemod.magic.mechanic.field.ModeSelection.selectable(com.example.examplemod.magic.SpellDefinition.find(state().selected()));
		for(var action:input.sample(down,modeRequested,state().selected()))send(action.action(),action.spell());
	}

	private MagicInputController(){}
}
