package com.derfa.playerinftrading;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerInfiniteTrading implements ModInitializer {
	public static final String MODID = "infinitetrading";
	private static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		ModCommands.register();
		LOGGER.info("Infinite Trading initialized");
	}
}
