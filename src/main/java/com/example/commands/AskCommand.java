package com.example.commands;

import com.example.services.LLMService;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class AskCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, LLMService llmService) {
		dispatcher.register(literal("ask")
				.then(argument("question", StringArgumentType.greedyString())
						.executes(context -> {
							String question = StringArgumentType.getString(context, "question");
							ServerCommandSource source = context.getSource();

							// Send initial response
							source.sendMessage(Text.literal("🤖 Thinking...").formatted(Formatting.YELLOW));

							// Process the question asynchronously
							new Thread(() -> {
								try {
									String response = llmService.askQuestion(question);
									source.sendMessage(Text.literal("🤖 " + response).formatted(Formatting.GREEN));
								} catch (Exception e) {
									source.sendMessage(
											Text.literal("❌ Error: " + e.getMessage()).formatted(Formatting.RED));
								}
							}).start();

							return 1;
						}))
				.executes(context -> {
					context.getSource()
							.sendMessage(Text.literal("Usage: /ask <your question>").formatted(Formatting.RED));
					return 0;
				}));
	}
}
