package com.example.examplemod.mixin;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.examplemod.ExampleMod;

/**
 * 示例 Mixin —— 用于验证 Mixin 配置是否生效。
 *
 * <p>本类向客户端主类 {@link Minecraft} 的 {@code run()} 方法注入，
 * 进入主循环时在日志中打印一条消息，证明 Mixin 已被 Fabric Loom 正确加载并应用。
 *
 * <p>本 Mixin 位于 {@code src/client} 源集，且 {@code mushokutenseiorigin.mixins.json}
 * 将其声明在 {@code client} 列表中，因此只在客户端生效。
 *
 * <p>验证方式：启动客户端（Minecraft Client），在控制台搜索
 * {@code ExampleMixin applied} 即可确认。
 */
@Mixin(Minecraft.class)
public class ExampleMixin {
	@Inject(method = "run", at = @At("HEAD"), require = 0)
	private void mushokutenseiorigin$onRunHead(CallbackInfo ci) {
		ExampleMod.LOGGER.info("[mushokutenseiorigin] ExampleMixin applied - Mixin configuration works!");
	}
}
