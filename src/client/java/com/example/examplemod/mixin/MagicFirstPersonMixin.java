package com.example.examplemod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import com.example.examplemod.magic.MagicAttachments;

@Mixin(ItemInHandRenderer.class)
public abstract class MagicFirstPersonMixin {
	@Shadow
	private void renderPlayerArm(PoseStack poses, SubmitNodeCollector collector, int light, float equip, float swing, HumanoidArm arm) {
		throw new AssertionError();
	}
	@Unique
	private static boolean casting(AbstractClientPlayer player, InteractionHand hand) {
		HumanoidArm arm = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
		return arm == HumanoidArm.RIGHT && !player.isUsingItem() && player.getAttachedOrElse(MagicAttachments.CAST_TICKS, 0) > 0;
	}
	@Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
	private void mushoku$raise(AbstractClientPlayer player, float delta, float pitch, InteractionHand hand, float swing, ItemStack stack,
			float equip, PoseStack poses, SubmitNodeCollector collector, int light, CallbackInfo ci) {
		if (casting(player, hand) && hand == InteractionHand.OFF_HAND && stack.isEmpty()) {
			// 空副手在外层统一补画，避免原版手部选择隐藏物理右手。
			ci.cancel();
			return;
		}
		if (casting(player, hand)) {
			poses.pushPose();
			poses.translate(-0.12, 0.18, -0.18);
			poses.mulPose(Axis.XP.rotationDegrees(-18));
		}
	}
	@Inject(method = "renderArmWithItem", at = @At("RETURN"))
	private void mushoku$restore(AbstractClientPlayer player, float delta, float pitch, InteractionHand hand, float swing, ItemStack stack,
			float equip, PoseStack poses, SubmitNodeCollector collector, int light, CallbackInfo ci) {
		if (casting(player, hand) && !(hand == InteractionHand.OFF_HAND && stack.isEmpty())) poses.popPose();
	}

	@Inject(method = "renderHandsWithItems", at = @At("RETURN"))
	private void mushoku$emptyRightHand(float delta, PoseStack poses, SubmitNodeCollector collector, LocalPlayer player, int light, CallbackInfo ci) {
		if (player.getMainArm() == HumanoidArm.LEFT && player.getOffhandItem().isEmpty()
				&& !player.isInvisible() && casting(player, InteractionHand.OFF_HAND)) {
			poses.pushPose();
			poses.translate(-0.12, 0.18, -0.18);
			poses.mulPose(Axis.XP.rotationDegrees(-18));
			renderPlayerArm(poses, collector, light, 0, 0, HumanoidArm.RIGHT);
			poses.popPose();
		}
	}
}
