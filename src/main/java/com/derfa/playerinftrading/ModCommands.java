package com.derfa.playerinftrading;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class ModCommands {

	private ModCommands() {}

	public static void register() {
		CommandRegistrationCallback.EVENT.register(ModCommands::registerCommands);
	}

	private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection) {
		dispatcher.register(Commands.literal("inftrade")
			.requires(source -> source.hasPermission(2))
			.then(Commands.literal("enable")
				.then(Commands.argument("players", EntityArgument.players())
					.executes(context -> manageTrading(context, true))))
			.then(Commands.literal("disable")
				.then(Commands.argument("players", EntityArgument.players())
					.executes(context -> manageTrading(context, false))))
		);
	}

	private static int manageTrading(CommandContext<CommandSourceStack> context, boolean enable) {
		try {
			Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
			InfiniteTradingData data = InfiniteTradingData.get(context.getSource().getLevel());

			for (ServerPlayer player : players) {
				if (enable) {
					data.addPlayer(player.getUUID());
					context.getSource().sendSuccess(() -> Component.literal("Enabled infinite trading for " + player.getScoreboardName()), true);
				} else {
					data.removePlayer(player.getUUID());
					context.getSource().sendSuccess(() -> Component.literal("Disabled infinite trading for " + player.getScoreboardName()), true);
				}
			}
			return players.size();
		} catch (Exception e) {
			context.getSource().sendFailure(Component.literal("Failed to modify trading: " + e.getMessage()));
			return 0;
		}
	}
}
