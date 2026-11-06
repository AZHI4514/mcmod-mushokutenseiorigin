package com.example.examplemod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/** 客户端通过 UseItemCallback 打开书页；学习请求由服务端验证实际持书。 */
public final class BasicAttackMagicPrimerItem extends Item {
	public BasicAttackMagicPrimerItem(Properties properties) {
		super(properties);
	}

	public static Properties createProperties() {
		return new Properties()
				.stacksTo(1);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		return InteractionResult.SUCCESS;
	}
}
