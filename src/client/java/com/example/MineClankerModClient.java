package com.example;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineClankerModClient implements ClientModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger("MineClankerModClient");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing MineClanker Mod Client!");
		// Client-side initialization if needed
	}
}
