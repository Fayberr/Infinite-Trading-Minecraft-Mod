package com.derfa.playerinftrading;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Collection;

@EventBusSubscriber(modid = PlayerInfiniteTrading.MODID)
public class ModCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

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
