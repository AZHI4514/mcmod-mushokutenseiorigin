package com.example.examplemod.client.magic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import com.example.examplemod.magic.SpellDefinition;
import com.example.examplemod.magic.MagicState;

import static com.example.examplemod.client.magic.MagicScreenStyle.*;

/** Read-only HUD rendering; no input or screen lifecycle. */
public final class MagicHud {
	public static void drawHud(GuiGraphicsExtractor g) {
		Minecraft client = Minecraft.getInstance();
		if (client.player == null || client.options.hideGui || client.player.isSpectator()) return;
		MagicState state = MagicClient.state();
		int preparing = client.player.getAttachedOrElse(com.example.examplemod.magic.MagicAttachments.PREPARE_LEFT, 0);
		int total = client.player.getAttachedOrElse(com.example.examplemod.magic.MagicAttachments.PREPARE_TOTAL, 0);
		if (preparing > 0 && total > 0) {
			var ritual=com.example.examplemod.magic.SpellDefinition.find(client.player.getAttachedOrElse(com.example.examplemod.magic.MagicAttachments.VISUAL_SPELL,""));
			if(ritual!=null&&ritual.castingProfile().requiresCircle()){int rx=g.guiWidth()/2-35,ry=g.guiHeight()-88;for(int layer=0;layer<7;layer++){int filled=(int)Math.clamp((long)(total-preparing)*7-layer*(long)total,0,total);g.fill(rx+layer*10,ry,rx+layer*10+8,ry+4,0xFF30223F);g.fill(rx+layer*10,ry,rx+layer*10+(int)(8L*filled/total),ry+4,0xFFCC9DFF);}g.centeredText(client.font,Component.translatable("magic.mushokutenseiorigin.ritual_layer",Math.min(7,1+(total-preparing)*7/total)),g.guiWidth()/2,ry-11,0xFFDCC6FF);}
			int bx = g.guiWidth() / 2 - 10, by = g.guiHeight() - 65;
			g.fill(bx, by, bx + 20, by + 5, 0xFF191919);
			g.fill(bx + 1, by + 1, bx + 1 + (int)(18L * (total - preparing) / total), by + 4, 0xFFCEB170);
			g.centeredText(client.font, String.format(java.util.Locale.ROOT, "%.1f", preparing / 20.0), bx + 10, by - 2, 0xFFFFFFFF);
		}
		// 原版一行 10 颗心：9 像素心形以 8 像素间距排列，总宽 81。
		// 九格栏宽 182、高 22；魔力槽在右侧，底边同为屏幕底边。
		int x = g.guiWidth() / 2 + 97;
		int barWidth = 81;
		// 最大 GUI 缩放下右侧可能不足 81 像素，整体等比缩放保留定位。
		float scale = Math.min(1.0F, Math.max(1, g.guiWidth() - x - 2) / (float) barWidth);
		g.pose().pushMatrix();
		g.pose().translate(x, g.guiHeight());
		g.pose().scale(scale, scale);
		g.fill(0, -7, barWidth, 0, 0xFF101A25);
		int filled = (int) (79L * state.mana() / state.maximum());
		g.fill(1, -6, 1 + filled, -1, 0xFF285BC5);
		g.fill(1, -6, 1 + filled, -5, 0xFF78BDFF);
		g.centeredText(client.font, state.mana() + " / " + state.maximum(), barWidth / 2, -18, 0xFF75BFFF);
		g.pose().popMatrix();
		int reserved=client.player.getAttachedOrElse(com.example.examplemod.magic.MagicAttachments.CHARGE_MANA,0);
		if (reserved>0) {
			g.centeredText(client.font,Integer.toString(com.example.examplemod.magic.mechanic.channel.ChargeCurve.display(reserved)),g.guiWidth()/2,g.guiHeight()/2+17,0xFFFFA090);
		}
		if (state.selected().isEmpty()) return;
		// 使用原版药水 HUD 的背景精灵和 24 像素行高，仅显示名字。
		int hudWidth = Math.max(24, client.font.width(name(state.selected())) + 12);
		int baseTop = client.isDemo() ? 16 : 1;
		int top = baseTop;
		// 原版增益/负面效果分别占用 26 像素的一行，避免盖住真实药水效果。
		for (var effect : client.player.getActiveEffects()) {
			if (effect.showIcon()) top = Math.max(top, baseTop + (effect.getEffect().value().isBeneficial() ? 26 : 52));
		}
		int left = g.guiWidth() - hudWidth - 1;
		g.blitSprite(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
				net.minecraft.resources.Identifier.withDefaultNamespace("hud/effect_background"), left, top, hudWidth, 24);
		int recovery = client.player.getAttachedOrElse(com.example.examplemod.magic.MagicAttachments.RECOVERY_TOTAL, 0);
		if (state.cooldown() > 0 && recovery > 0) {
			int height = Math.clamp((int)(24L * state.cooldown() / recovery), 0, 24);
			g.fill(left, top + 24 - height, left + hudWidth, top + 24, 0x80000000);
		}
		g.centeredText(client.font, name(state.selected()), left + hudWidth / 2, top + 8, 0xFFFFFFFF);
	}
	private MagicHud() {}
}
