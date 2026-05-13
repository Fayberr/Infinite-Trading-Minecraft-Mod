package com.derfa.playerinftrading;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class ModCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("infinitetrading")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                    .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                        .executes(context -> {
                            Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "targets");
                            boolean enabled = BoolArgumentType.getBool(context, "enabled");
                            InfiniteTradingData data = InfiniteTradingData.get(context.getSource().getWorld());

                            for (ServerPlayerEntity player : players) {
                                data.setInfinite(player.getUuid(), enabled);
                            }

                            context.getSource().sendFeedback(() -> Text.literal("Set infinite trading to " + enabled + " for " + players.size() + " players"), true);
                            return players.size();
                        })
                    )
                )
            );
        });
    }
}
