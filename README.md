# MineClanker

A **client-side** Minecraft Fabric mod that allows you to ask questions to GPT-5 nano via chat commands

## Features

- `/ask` command to get AI responses from GPT-5 nano
- **Configuration** - customize max tokens, verbosity, reasoning, and system prompt
- In-game API key management with `/setapikey`, `/removeapikey`, and `/apikeystatus`
- **Client-side only** - works on any server without server installation
- **Persistent configuration** - all settings saved and persist between game sessions
- **Official OpenAI Java SDK 3.0.0** - latest sdk
- **GPT-5 nano features** - verbosity and reasoning control
- Asynchronous processing to avoid blocking the game
- Configurable API settings
- No need to restart the game after setting API key

## Requirements

- Minecraft 1.21.8
- Fabric Loader 0.16.14+
- Java 21+
- OpenAI API key

## Setup

### 1. Get an OpenAI API Key

1. Go to [OpenAI's website](https://platform.openai.com/)
2. Create an account and get an API key
3. **Important**: You can now set the API key in-game using `/setapikey <key>`

### 2. Build the Mod (Or just download it in releases)

```bash
./gradlew build
```

The mod JAR will be created in `build/libs/`

### 3. Install

1. Copy the JAR file to your **local Minecraft mods folder**
2. Start Minecraft with Fabric Loader
3. The mod should load automatically

## Usage

### AI Chat Commands

In-game, use the command:

```
/ask <question>
```

### API Key Management

**Set API Key:**

```
/setapikey <your-api-key-here>
```

**Remove API Key:**

```
/removeapikey
```

**Check API Key Status:**

```
/apikeystatus
```

### Configuration Commands

**Set Max Tokens (1-4000):**

```
/settokens <number>
```

**Set Verbosity Level:**

```
/setverbosity <low|medium|high>
```

**Set Reasoning Effort:**

```
/setreasoning <minimal|low|medium|high>
```

**Set System Prompt:**

```
/setsystemprompt <your custom prompt>
```

**View Current Configuration:**

```
/config
```

**Note**: Your API key and all configuration settings are stored locally on your computer and only you can use them. They're automatically saved and will persist between game sessions.

## How It Works

This is a **client-side** mod that:

- Runs entirely on your local Minecraft client
- Uses the **official OpenAI Java SDK 3.0.0** for reliable API communication
- Stores your API key and configuration locally in the Minecraft config folder
- No server setup or permissions required
- **Persistent storage** - your settings are remembered between game sessions

## Configuration Options

### **Max Tokens**

- **Range**: 1-4000 tokens
- **Default**: 1000 tokens
- **Effect**: Controls response length (higher = longer responses, more cost)

### **Verbosity**

- **Options**: `low`, `medium`, `high`
- **Default**: `low`
- **Effect**: Controls how detailed responses are

### **Reasoning Effort**

- **Options**: `minimal`, `low`, `medium`, `high`
- **Default**: `minimal`
- **Effect**: Controls how much the AI thinks about responses (higher = more thoughtful but slower)

### **System Prompt**

- **Default**: "Answer questions about up to date Minecraft gameplay and mechanics concisely and accurately. Keep your responses brief and focused."
- **Effect**: Sets the AI's personality and behavior for all responses

## API Key Storage

Your API key and configuration are stored in a file called `mineclanker_config.json` in your Minecraft `config` folder. This file:

- Is created automatically when you set your API key
- Contains your API key and all configuration settings
- Is stored locally on your computer in the proper config location
- Persists between game sessions
- Is automatically loaded when you start the game

**Location**: `.minecraft/config/mineclanker_config.json`

**Example config file:**

```json
{
  "apiKey": "your-api-key-here",
  "maxTokens": 200,
  "verbosity": "low",
  "reasoning": "minimal",
  "systemPrompt": "You are a helpful Minecraft assistant..."
}
```

## Configuration

The mod uses these default settings:

- Model: `gpt-5-nano` (latest and cheapest)
- Max tokens: 200 (short responses)
- **Verbosity**: `low` (concise responses)
- **Reasoning effort**: `minimal` (fast responses)

## GPT-5 Nano Features

The mod leverages GPT-5 nano's advanced capabilities:

- **Verbosity Control**: Set to "low" for concise, Minecraft-focused answers
- **Reasoning Effort**: Set to "minimal" for faster response times
- **Cost Optimization**: Efficient token usage for gaming queries

## Cost Information

- GPT-5-nano costs about $0.05/1M input tokens, $0.40/1M output tokens
- Pretty cheap

## Troubleshooting

**"No API key set!"**

- Use `/setapikey <your-key>` to set your API key
- Your API key is stored locally on your computer and persists between sessions

**API call failed**

- Check your internet connection
- Verify your API key is correct using `/apikeystatus`
- Check your OpenAI account balance

**Mod not loading**

- Ensure you have Fabric Loader installed
- Check the Minecraft logs for error messages
- Verify the mod JAR is in the correct mods folder
