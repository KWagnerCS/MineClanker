package com.ftkevon.mineclanker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LLMService {
	private static final String CONFIG_FILE = "config/mineclanker_config.json";
	private String apiKey = null;
	private OpenAIClient client = null;

	// Configurable settings with defaults
	private int maxOutputTokens = 1000;
	private String verbosity = "low";
	private String reasoning = "minimal";
	private String systemPrompt = "Answer questions about up to date Minecraft gameplay and mechanics concisely and accurately. Keep your responses brief and focused.";

	// Jackson ObjectMapper for JSON handling
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public LLMService() {
		// Load configuration from config file on startup
		loadConfigFromFile();
		// Initialize OpenAI client if API key is available
		initializeOpenAIClient();
	}

	public String askQuestion(String question) throws IOException {
		if (apiKey == null || apiKey.isEmpty()) {
			throw new RuntimeException("No API key set! Use /setapikey <key> to set one.");
		}

		if (client == null) {
			initializeOpenAIClient();
		}

		try {
			ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
					.model(ChatModel.GPT_5_NANO) // Use GPT-5 Nano
					.addSystemMessage(systemPrompt)
					.addUserMessage(question)
					.maxCompletionTokens(maxOutputTokens)
					.build();

			ChatCompletion completion = client.chat().completions().create(createParams);

			StringBuilder answerBuilder = new StringBuilder();
			completion.choices().stream()
				.flatMap(choice -> choice.message().content().stream())
				.forEach(answerBuilder::append);
			
			String answer = answerBuilder.toString();
			return answer.isBlank() ? "Error with response" : answer;

		} catch (Exception e) {
			throw new IOException("Failed to get response from OpenAI: " + e.getMessage(), e);
		}
	}

	// Method to set API key for this client and save to config
	public void setApiKey(String newApiKey) {
		if (newApiKey == null || newApiKey.trim().isEmpty()) {
			throw new IllegalArgumentException("API key cannot be null or empty");
		}
		this.apiKey = newApiKey.trim();
		// Save to config file
		saveConfigToFile();
		// Reinitialize OpenAI client with new key
		initializeOpenAIClient();
	}

	// Method to remove API key for this client and clear config
	public void removeApiKey() {
		this.apiKey = null;
		this.client = null;
		// Clear config file
		clearConfigFile();
	}

	// Method to check if API key is set for this client
	public boolean hasApiKey() {
		return apiKey != null && !apiKey.trim().isEmpty();
	}

	// Configuration methods
	public void setMaxTokens(int maxTokens) {
		if (maxTokens < 1 || maxTokens > 4000) {
			throw new IllegalArgumentException("Max tokens must be between 1 and 4000");
		}
		this.maxOutputTokens = maxTokens;
		saveConfigToFile();
	}

	public int getMaxTokens() {
		return maxOutputTokens;
	}

	public void setVerbosity(String verbosity) {
		if (!verbosity.equals("low") && !verbosity.equals("medium") && !verbosity.equals("high")) {
			throw new IllegalArgumentException("Verbosity must be 'low', 'medium', or 'high'");
		}
		this.verbosity = verbosity;
		saveConfigToFile();
	}

	public String getVerbosity() {
		return verbosity;
	}

	public void setReasoning(String reasoning) {
		if (!reasoning.equals("minimal") && !reasoning.equals("low") && !reasoning.equals("medium")
				&& !reasoning.equals("high")) {
			throw new IllegalArgumentException("Reasoning must be 'minimal', 'low', 'medium', or 'high'");
		}
		this.reasoning = reasoning;
		saveConfigToFile();
	}

	public String getReasoning() {
		return reasoning;
	}

	public void setSystemPrompt(String systemPrompt) {
		if (systemPrompt == null || systemPrompt.trim().isEmpty()) {
			throw new IllegalArgumentException("System prompt cannot be null or empty");
		}
		this.systemPrompt = systemPrompt.trim();
		saveConfigToFile();
	}

	public String getSystemPrompt() {
		return systemPrompt;
	}








	
	// Initialize OpenAI client with current API key
	private void initializeOpenAIClient() {
		if (apiKey != null && !apiKey.trim().isEmpty()) {
			// Create client with API key
			this.client = OpenAIOkHttpClient.builder()
				.apiKey(apiKey)
				.build();
		}
	}








	// Load configuration from config file
	private void loadConfigFromFile() {
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

				// Load API key
				if (config.has("apiKey")) {
					this.apiKey = config.get("apiKey").asText();
				}

				// Load other settings with defaults
				if (config.has("maxTokens")) {
					this.maxOutputTokens = config.get("maxTokens").asInt();
				}
				if (config.has("verbosity")) {
					this.verbosity = config.get("verbosity").asText();
				}
				if (config.has("reasoning")) {
					this.reasoning = config.get("reasoning").asText();
				}
				if (config.has("systemPrompt")) {
					this.systemPrompt = config.get("systemPrompt").asText();
				}
			}
		} catch (Exception e) {
			// Silently fail if config can't be loaded
			System.err.println("Could not load MineClanker config: " + e.getMessage());
		}
	}

	// Save configuration to config file
	private void saveConfigToFile() {
		try {
			// Create config object using Jackson
			JsonNode config = objectMapper.createObjectNode()
					.put("apiKey", this.apiKey != null ? this.apiKey : "")
					.put("maxTokens", this.maxOutputTokens)
					.put("verbosity", this.verbosity)
					.put("reasoning", this.reasoning)
					.put("systemPrompt", this.systemPrompt);

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

	// Clear configuration file
	private void clearConfigFile() {
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
