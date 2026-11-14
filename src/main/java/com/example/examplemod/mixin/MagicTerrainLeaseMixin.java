package com.example.examplemod.mixin;

import com.example.examplemod.magic.TerrainLeaseRepository;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public abstract class MagicTerrainLeaseMixin {
	@Inject(method="setBlockState",at=@At("RETURN"))
	private void changed(BlockPos pos,BlockState state,int flags,CallbackInfoReturnable<BlockState> cir){
		var chunk=(LevelChunk)(Object)this;
		if(chunk.getLevel() instanceof ServerLevel level){TerrainLeaseRepository.externalChange(level,pos,cir.getReturnValue(),state);com.example.examplemod.magic.LightLeases.externalChange(level,pos,cir.getReturnValue(),state);}
	}
}
