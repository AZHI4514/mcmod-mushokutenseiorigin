package com.example.examplemod.mixin;
import com.example.examplemod.client.magic.IllusionProjection;
import net.minecraft.client.renderer.block.*;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ModelBlockRenderer.class)
abstract class MagicIllusionBlocksMixin {
	@Inject(method="tesselateBlock",at=@At("HEAD"),cancellable=true)
	private void magic$terrain(BlockQuadOutput output,float x,float y,float z,BlockAndTintGetter world,BlockPos pos,BlockState state,BlockStateModel model,long seed,CallbackInfo info){if(IllusionProjection.masked(pos))info.cancel();}
}
