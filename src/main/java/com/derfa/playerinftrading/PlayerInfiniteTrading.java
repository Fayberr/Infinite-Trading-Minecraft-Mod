package com.derfa.playerinftrading;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerInfiniteTrading implements ModInitializer {
    public static final String MOD_ID = "player_infinite_trading";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModCommands.register();
        LOGGER.info("Player Infinite Trading initialized!");
    }
}
