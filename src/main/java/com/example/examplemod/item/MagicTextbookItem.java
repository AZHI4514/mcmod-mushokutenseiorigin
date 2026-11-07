package com.example.examplemod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import com.example.examplemod.magic.MagicCategory;

/** 分类教材的等级和课程由服务端校验，不读取客户端提供的目录。 */
public final class MagicTextbookItem extends Item {
	private final MagicCategory category;
	private final int rank;
	public MagicTextbookItem(Properties properties, MagicCategory category, int rank) {
		super(properties);
		this.category = category;
		this.rank = rank;
	}
	public MagicCategory category() { return category; }
	public int rank() { return rank; }
	public static Properties createProperties() { return new Properties().stacksTo(1); }
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) { return InteractionResult.SUCCESS; }
}
