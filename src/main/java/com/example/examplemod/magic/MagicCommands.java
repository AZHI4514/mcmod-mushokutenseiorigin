package com.example.examplemod.magic;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

/** 所有管理操作沿用原版作弊权限，数值只由服务端修改。 */
public final class MagicCommands {
	public static void initialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> {
			var command = Commands.literal("mtmagic").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
			command.then(Commands.literal("maxmana")
					.then(Commands.argument("players", EntityArgument.players())
							.then(Commands.argument("amount", IntegerArgumentType.integer(1, 1000000))
									.executes(context -> {
										var players = EntityArgument.getPlayers(context, "players");
										int amount = IntegerArgumentType.getInteger(context, "amount");
										players.forEach(player -> MagicSystem.setMaximum(player, amount));
										context.getSource().sendSuccess(() -> Component.translatable(
												"magic.mushokutenseiorigin.maximum_changed", players.size(), amount), true);
										return players.size();
									}))));
			command.then(Commands.literal("restore")
					.then(Commands.argument("players", EntityArgument.players())
							.then(Commands.literal("full").executes(context -> restore(context, Integer.MAX_VALUE)))
							.then(Commands.argument("amount", IntegerArgumentType.integer(1))
									.executes(context -> restore(context, IntegerArgumentType.getInteger(context, "amount"))))));
			dispatcher.register(command);
		});
	}

	private static int restore(CommandContext<CommandSourceStack> context, int amount) throws CommandSyntaxException {
		var players = EntityArgument.getPlayers(context, "players");
		long restored = 0;
		for (var player : players) {
			int before = MagicSystem.state(player).mana();
			MagicSystem.restoreMana(player, amount);
			restored += MagicSystem.state(player).mana() - before;
		}
		long total = restored;
		context.getSource().sendSuccess(() -> Component.translatable(
				"magic.mushokutenseiorigin.mana_restored", players.size(), total), true);
		return players.size();
	}

	private MagicCommands() {
	}
}
