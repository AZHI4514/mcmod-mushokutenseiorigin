package com.example.examplemod.mixin;
import com.example.examplemod.client.magic.IllusionProjection;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(BlockEntityRenderDispatcher.class)
abstract class MagicIllusionBlockEntityMixin {
	@Inject(method="tryExtractRenderState",at=@At("HEAD"),cancellable=true)
	private void magic$conceal(BlockEntity block,float delta,ModelFeatureRenderer.CrumblingOverlay overlay,CallbackInfoReturnable<BlockEntityRenderState> info){if(IllusionProjection.masked(block.getBlockPos()))info.setReturnValue(null);}
}
