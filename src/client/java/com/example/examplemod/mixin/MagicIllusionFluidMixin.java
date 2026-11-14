package com.example.examplemod.mixin;
import com.example.examplemod.client.magic.IllusionProjection;
import net.minecraft.client.renderer.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(FluidRenderer.class)
abstract class MagicIllusionFluidMixin {
	@Inject(method="tesselate",at=@At("HEAD"),cancellable=true)
	private void magic$fluid(BlockAndTintGetter world,BlockPos pos,FluidRenderer.Output output,BlockState block,FluidState fluid,CallbackInfo info){if(IllusionProjection.masked(pos))info.cancel();}
}
