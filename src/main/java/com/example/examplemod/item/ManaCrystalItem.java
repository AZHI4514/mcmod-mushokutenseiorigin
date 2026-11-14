package com.example.examplemod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import com.example.examplemod.magic.MagicAttachments;
import com.example.examplemod.magic.MagicSystem;

/** Right-click to charge one crystal using the player's own mana. */
public final class ManaCrystalItem extends Item {
	public static final int COST = 25;

	public ManaCrystalItem(Properties properties) {
		super(properties);
	}

	public static Properties createProperties() {
		return new Properties().stacksTo(64);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (!(player instanceof ServerPlayer server)) return InteractionResult.SUCCESS;
		if (!server.isAlive() || server.isSpectator()) return InteractionResult.FAIL;
		var before = MagicSystem.state(server);
		if (before.mana() < COST) {
			server.sendSystemMessage(Component.translatable("magic.mushokutenseiorigin.crystal_insufficient"), true);
			return InteractionResult.FAIL;
		}
		var held = server.getItemInHand(hand);
		var charged = new ItemStack(ModItems.CHARGED_MANA_CRYSTAL);
		if (held.getCount() == 1) {
			server.setItemInHand(hand, charged);
		} else {
			if (!server.getInventory().add(charged)) {
				server.sendSystemMessage(Component.translatable("magic.mushokutenseiorigin.crystal_no_space"), true);
				return InteractionResult.FAIL;
			}
			held.shrink(1);
		}
		server.setAttached(MagicAttachments.STATE, before.spendMana(COST));
		return InteractionResult.SUCCESS;
	}
}
