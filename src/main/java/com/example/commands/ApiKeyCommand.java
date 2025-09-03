package com.example.commands;

import com.example.services.LLMService;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ApiKeyCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, LLMService llmService) {
		// Command to set API key
		dispatcher.register(literal("setapikey")
				.then(argument("key", StringArgumentType.string())
						.executes(context -> {
							String apiKey = StringArgumentType.getString(context, "key");
							ServerCommandSource source = context.getSource();

							try {
								llmService.setApiKey(apiKey);
								source.sendMessage(
										Text.literal("✅ API key set successfully!").formatted(Formatting.GREEN));
							} catch (Exception e) {
								source.sendMessage(Text.literal("❌ Failed to set API key: " + e.getMessage())
										.formatted(Formatting.RED));
							}

							return 1;
						}))
				.executes(context -> {
					context.getSource()
							.sendMessage(Text.literal("Usage: /setapikey <your-api-key>").formatted(Formatting.RED));
					return 0;
				}));

		// Command to remove API key
		dispatcher.register(literal("removeapikey")
				.executes(context -> {
					ServerCommandSource source = context.getSource();

					try {
						llmService.removeApiKey();
						source.sendMessage(Text.literal("✅ API key removed successfully!").formatted(Formatting.GREEN));
					} catch (Exception e) {
						source.sendMessage(Text.literal("❌ Failed to remove API key: " + e.getMessage())
								.formatted(Formatting.RED));
					}

					return 1;
				}));

		// Command to check API key status
		dispatcher.register(literal("apikeystatus")
				.executes(context -> {
					ServerCommandSource source = context.getSource();

					if (llmService.hasApiKey()) {
						source.sendMessage(
								Text.literal("✅ API key is set and ready to use!").formatted(Formatting.GREEN));
					} else {
						source.sendMessage(Text.literal("❌ No API key set. Use /setapikey <key> to set one.")
								.formatted(Formatting.RED));
					}

					return 1;
				}));
	}
}
