package com.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class LLMService {
	private static final String CONFIG_FILE = "config/mineclanker_config.json";
	private String apiKey = null;
	private OpenAiService openAiService = null;

	// Jackson ObjectMapper for JSON handling
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public LLMService() {
		// Load API key from config file on startup
		loadApiKeyFromConfig();
		// Initialize OpenAI service if API key is available
		initializeOpenAiService();
	}

	public String askQuestion(String question) throws IOException {
		if (apiKey == null || apiKey.isEmpty()) {
			throw new RuntimeException("No API key set! Use /setapikey <key> to set one.");
		}

		if (openAiService == null) {
			initializeOpenAiService();
		}

		try {
			// Create chat completion request using the official SDK
			ChatCompletionRequest request = ChatCompletionRequest.builder()
					.model("gpt-5-nano")
					.messages(List.of(new ChatMessage("user", question)))
					.maxTokens(200)
					.temperature(0.7)
					.build();

			// Get response from OpenAI
			String response = openAiService.createChatCompletion(request)
					.getChoices().get(0).getMessage().getContent();

			return response;
		} catch (Exception e) {
			throw new IOException("API call failed: " + e.getMessage(), e);
		}
	}

	// Method to set API key for this client and save to config
	public void setApiKey(String newApiKey) {
		if (newApiKey == null || newApiKey.trim().isEmpty()) {
			throw new IllegalArgumentException("API key cannot be null or empty");
		}
		this.apiKey = newApiKey.trim();
		// Save to config file
		saveApiKeyToConfig();
		// Reinitialize OpenAI service with new key
		initializeOpenAiService();
	}

	// Method to remove API key for this client and clear config
	public void removeApiKey() {
		this.apiKey = null;
		this.openAiService = null;
		// Clear config file
		clearApiKeyFromConfig();
	}

	// Method to check if API key is set for this client
	public boolean hasApiKey() {
		return apiKey != null && !apiKey.trim().isEmpty();
	}

	// Initialize OpenAI service with current API key
	private void initializeOpenAiService() {
		if (apiKey != null && !apiKey.trim().isEmpty()) {
			// Create service with optimized timeouts
			this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(30));
		}
	}

	// Load API key from config file
	private void loadApiKeyFromConfig() {
		try {
			Path configPath = Paths.get(CONFIG_FILE);
			// Ensure config directory exists
			Path configDir = configPath.getParent();
			if (configDir != null && !Files.exists(configDir)) {
				Files.createDirectories(configDir);
			}

			if (Files.exists(configPath)) {
				String configContent = Files.readString(configPath);
				JsonNode config = objectMapper.readTree(configContent);
				if (config.has("apiKey")) {
					this.apiKey = config.get("apiKey").asText();
				}
			}
		} catch (Exception e) {
			// Silently fail if config can't be loaded
			System.err.println("Could not load MineClanker config: " + e.getMessage());
		}
	}

	// Save API key to config file
	private void saveApiKeyToConfig() {
		try {
			// Create config object using Jackson
			JsonNode config = objectMapper.createObjectNode()
					.put("apiKey", this.apiKey);

			Path configPath = Paths.get(CONFIG_FILE);
			// Ensure config directory exists
			Path configDir = configPath.getParent();
			if (configDir != null && !Files.exists(configDir)) {
				Files.createDirectories(configDir);
			}

			Files.writeString(configPath, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(config));
		} catch (Exception e) {
			System.err.println("Could not save MineClanker config: " + e.getMessage());
		}
	}

	// Clear API key from config file
	private void clearApiKeyFromConfig() {
		try {
			Path configPath = Paths.get(CONFIG_FILE);
			if (Files.exists(configPath)) {
				Files.delete(configPath);
			}
		} catch (Exception e) {
			System.err.println("Could not clear MineClanker config: " + e.getMessage());
		}
	}

	// Alternative method for testing without API key
	public String askQuestionLocal(String question) {
		return "This is a test response. Use /setapikey <key> to set your OpenAI API key for real AI responses!";
	}
}
