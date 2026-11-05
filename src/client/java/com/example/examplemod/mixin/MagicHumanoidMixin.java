package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import com.example.examplemod.client.magic.MagicPoseState;

@Mixin(HumanoidModel.class)
public abstract class MagicHumanoidMixin {
	@Shadow @Final public ModelPart rightArm;
	@Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
	private void mushoku$pose(HumanoidRenderState state, CallbackInfo ci) {
		if (((MagicPoseState) state).mushoku$isCasting()) {
			rightArm.xRot = -1.35F;
			rightArm.yRot = -0.1F;
			rightArm.zRot = 0.0F;
		}
	}
}
