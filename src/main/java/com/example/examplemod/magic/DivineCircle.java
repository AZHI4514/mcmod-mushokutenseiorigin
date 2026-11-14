package com.example.examplemod.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import com.example.examplemod.item.ModItems;

/** Ordinary divine circle, anchored to the same floor for the entire chant. */
public final class DivineCircle {
	public static final int REQUIRED_CRYSTALS = 8;

	public static BlockPos locate(ServerPlayer player) {
		var floor = player.blockPosition().below();
		for (int x = -3; x <= 3; x++) for (int z = -3; z <= 3; z++) {
			var center = floor.offset(x, 0, z);
			if (valid(player, center)) return center;
		}
		return null;
	}

	static boolean rune(int x, int z) {
		return Math.abs(x) == 2 && Math.abs(z) == 2
				|| Math.abs(x) == 3 && z == 0 || Math.abs(z) == 3 && x == 0;
	}

	public static boolean valid(ServerPlayer player, BlockPos center) {
		var level = (ServerLevel) player.level();
		if (!withinRange(player.position(), center) || crystals(player) < REQUIRED_CRYSTALS) return false;
		if (!level.hasChunkAt(center) || !level.getBlockState(center).is(Blocks.LODESTONE)) return false;
		for (int x = -3; x <= 3; x++) for (int z = -3; z <= 3; z++) {
			if (x == 0 && z == 0) continue;
			var pos = center.offset(x, 0, z);
			if (!level.hasChunkAt(pos) || !level.getBlockState(pos).is(rune(x, z) ? Blocks.AMETHYST_BLOCK : Blocks.POLISHED_DEEPSLATE)) return false;
		}
		for (int i = 1; i <= 3; i++) if (!level.getBlockState(center.above(i)).isAir()) return false;
		return true;
	}

	static boolean withinRange(Vec3 feet, BlockPos center) {
		return feet.distanceToSqr(Vec3.atBottomCenterOf(center.above())) <= 9;
	}

	private static int crystals(ServerPlayer player) {
		int count = 0;
		var inventory = player.getInventory();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			var stack = inventory.getItem(i);
			if (stack.is(ModItems.CHARGED_MANA_CRYSTAL)) count += stack.getCount();
		}
		return count;
	}

	public static void consume(ServerPlayer player) {
		int left = REQUIRED_CRYSTALS;
		var inventory = player.getInventory();
		for (int i = 0; i < inventory.getContainerSize() && left > 0; i++) {
			var stack = inventory.getItem(i);
			if (!stack.is(ModItems.CHARGED_MANA_CRYSTAL)) continue;
			int used = Math.min(left, stack.getCount());
			stack.shrink(used);
			left -= used;
		}
		inventory.setChanged();
	}

	public static void glow(ServerPlayer player, BlockPos center, int elapsed) {
		var level = (ServerLevel) player.level();
		var p = Vec3.atBottomCenterOf(center.above());
		for (int i = 0; i < 16; i++) {
			double a = i * Math.PI / 8 + elapsed * .034;
			double radius = i % 2 == 0 ? 2.75 : 1.25;
			level.sendParticles(ParticleTypes.ENCHANT, p.x + Math.cos(a) * radius, p.y + .12,
					p.z + Math.sin(a) * radius, 2, .08, .13, .08, .025);
		}
	}

	private DivineCircle() {}
}
