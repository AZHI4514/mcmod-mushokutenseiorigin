package com.example.examplemod.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.ClockTimeMarker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.example.examplemod.magic.MagicSystem;

/** 仅原版跳夜成功的那一次集体唤醒恢复魔力。 */
@Mixin(ServerLevel.class)
public abstract class MagicSleepMixin {
	@Unique private boolean mushoku$advancedMorning;
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/clock/ServerClockManager;moveToTimeMarker(Lnet/minecraft/core/Holder;Lnet/minecraft/resources/ResourceKey;)Z"))
	private boolean mushoku$skipNight(ServerClockManager clocks, Holder<WorldClock> clock, ResourceKey<ClockTimeMarker> marker) {
		long before = clocks.getTotalTicks(clock);
		boolean advanced = clocks.moveToTimeMarker(clock, marker);
		mushoku$advancedMorning = advanced && clocks.getTotalTicks(clock) > before;
		return advanced;
	}
	@Inject(method = "wakeUpAllPlayers", at = @At("HEAD"))
	private void mushoku$restore(CallbackInfo ci) {
		if (mushoku$advancedMorning) {
			for (var player : ((ServerLevel) (Object) this).players())
				if (player.isSleeping()) MagicSystem.successfulSleep(player);
		}
		mushoku$advancedMorning = false;
	}
}
