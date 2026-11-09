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
import com.example.examplemod.client.magic.MagicCastMotion;

@Mixin(HumanoidModel.class)
public abstract class MagicHumanoidMixin {
	@Shadow @Final public ModelPart rightArm;
	@Shadow @Final public ModelPart leftArm;
	@Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
	private void mushoku$pose(HumanoidRenderState state, CallbackInfo ci) {
		if (((MagicPoseState) state).mushoku$isCasting()) {
			float time=state.ageInTicks;
			rightArm.xRot = -1.35F+MagicCastMotion.pitch(time);
			rightArm.yRot = -0.1F+MagicCastMotion.yaw(time);
			rightArm.zRot = MagicCastMotion.roll(time);
			leftArm.xRot = -1.0F+MagicCastMotion.pitch(time+4)*.65F;
			leftArm.yRot = 0.55F+MagicCastMotion.yaw(time+4)*.6F;
			leftArm.zRot = -0.35F+MagicCastMotion.roll(time+4)*.6F;
		}
	}
}
