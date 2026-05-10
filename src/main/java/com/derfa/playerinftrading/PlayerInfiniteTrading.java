package com.derfa.playerinftrading;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(PlayerInfiniteTrading.MODID)
public class PlayerInfiniteTrading {
    public static final String MODID = "playerinftrading";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PlayerInfiniteTrading(IEventBus modEventBus) {
        LOGGER.info("Player Infinite Trading Initialized");
    }
}
