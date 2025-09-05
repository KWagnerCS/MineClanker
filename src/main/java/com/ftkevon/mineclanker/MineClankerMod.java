package com.ftkevon.mineclanker;

import com.ftkevon.mineclanker.commands.AskCommand;
import com.ftkevon.mineclanker.commands.ApiKeyCommand;
import com.ftkevon.mineclanker.services.LLMService;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MineClankerMod implements ModInitializer {
	public static final String MOD_ID = "mineclanker";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static LLMService llmService;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing MineClanker Mod!");

		// Initialize LLM service
		llmService = new LLMService();

		// Register commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			AskCommand.register(dispatcher, llmService);
			ApiKeyCommand.register(dispatcher, llmService);
		});

		LOGGER.info("MineClanker Mod initialized successfully!");
	}

	public static LLMService getLLMService() {
		return llmService;
	}
}
