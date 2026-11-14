package com.example.examplemod.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.example.examplemod.infrastructure.fabric.magic.HolySanctuaryAdapter;

@Mixin(SpawnPlacements.class)
abstract class MagicSpawnMixin {
	@Inject(method = "checkSpawnRules", at = @At("HEAD"), cancellable = true)
	private static void magic$preventNaturalUndead(EntityType<?> type, ServerLevelAccessor level,
			EntitySpawnReason reason, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> info) {
		if (reason == EntitySpawnReason.NATURAL && HolySanctuaryAdapter.suppresses(level.getLevel(), type, pos)) {
			info.setReturnValue(false);
		}
	}
}
