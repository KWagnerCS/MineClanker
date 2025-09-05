package com.ftkevon.mineclanker.commands;

import com.ftkevon.mineclanker.services.LLMService;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
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

		// Command to set max tokens
		dispatcher.register(literal("settokens")
				.then(argument("tokens", IntegerArgumentType.integer(1, 4000))
						.executes(context -> {
							int tokens = IntegerArgumentType.getInteger(context, "tokens");
							ServerCommandSource source = context.getSource();

							try {
								llmService.setMaxTokens(tokens);
								source.sendMessage(Text.literal("✅ Max tokens set to " + tokens + "!")
										.formatted(Formatting.GREEN));
							} catch (Exception e) {
								source.sendMessage(Text.literal("❌ Failed to set max tokens: " + e.getMessage())
										.formatted(Formatting.RED));
							}

							return 1;
						}))
				.executes(context -> {
					context.getSource()
							.sendMessage(Text.literal("Usage: /settokens <1-4000>").formatted(Formatting.RED));
					return 0;
				}));

		// Command to set verbosity
		dispatcher.register(literal("setverbosity")
				.then(argument("level", StringArgumentType.word())
						.executes(context -> {
							String verbosity = StringArgumentType.getString(context, "level");
							ServerCommandSource source = context.getSource();

							try {
								llmService.setVerbosity(verbosity);
								source.sendMessage(Text.literal("✅ Verbosity set to " + verbosity + "!")
										.formatted(Formatting.GREEN));
							} catch (Exception e) {
								source.sendMessage(Text.literal("❌ Failed to set verbosity: " + e.getMessage())
										.formatted(Formatting.RED));
							}

							return 1;
						}))
				.executes(context -> {
					context.getSource()
							.sendMessage(
									Text.literal("Usage: /setverbosity <low|medium|high>").formatted(Formatting.RED));
					return 0;
				}));

		// Command to set reasoning effort
		dispatcher.register(literal("setreasoning")
				.then(argument("level", StringArgumentType.word())
						.executes(context -> {
							String reasoning = StringArgumentType.getString(context, "level");
							ServerCommandSource source = context.getSource();

							try {
								llmService.setReasoning(reasoning);
								source.sendMessage(Text.literal("✅ Reasoning effort set to " + reasoning + "!")
										.formatted(Formatting.GREEN));
							} catch (Exception e) {
								source.sendMessage(Text.literal("❌ Failed to set reasoning: " + e.getMessage())
										.formatted(Formatting.RED));
							}

							return 1;
						}))
				.executes(context -> {
					context.getSource()
							.sendMessage(Text.literal("Usage: /setreasoning <minimal|low|medium|high>")
									.formatted(Formatting.RED));
					return 0;
				}));

		// Command to set system prompt
		dispatcher.register(literal("setsystemprompt")
				.then(argument("prompt", StringArgumentType.greedyString())
						.executes(context -> {
							String prompt = StringArgumentType.getString(context, "prompt");
							ServerCommandSource source = context.getSource();

							try {
								llmService.setSystemPrompt(prompt);
								source.sendMessage(
										Text.literal("✅ System prompt updated!").formatted(Formatting.GREEN));
							} catch (Exception e) {
								source.sendMessage(Text.literal("❌ Failed to set system prompt: " + e.getMessage())
										.formatted(Formatting.RED));
							}

							return 1;
						}))
				.executes(context -> {
					context.getSource()
							.sendMessage(Text.literal("Usage: /setsystemprompt <your system prompt>")
									.formatted(Formatting.RED));
					return 0;
				}));

		// Command to show current configuration
		dispatcher.register(literal("config")
				.executes(context -> {
					ServerCommandSource source = context.getSource();

					source.sendMessage(Text.literal("🔧 MineClanker Configuration:").formatted(Formatting.GOLD));
					source.sendMessage(Text.literal("API Key: " + (llmService.hasApiKey() ? "✅ Set" : "❌ Not set"))
							.formatted(Formatting.WHITE));
					source.sendMessage(
							Text.literal("Max Tokens: " + llmService.getMaxTokens()).formatted(Formatting.WHITE));
					source.sendMessage(
							Text.literal("Verbosity: " + llmService.getVerbosity()).formatted(Formatting.WHITE));
					source.sendMessage(
							Text.literal("Reasoning: " + llmService.getReasoning()).formatted(Formatting.WHITE));
					source.sendMessage(
							Text.literal("System Prompt: " + llmService.getSystemPrompt()).formatted(Formatting.WHITE));

					return 1;
				}));
	}
}
