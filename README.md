# MineClanker

A **client-side** Minecraft Fabric mod that allows you to ask questions to GPT-5 nano via chat commands

## Features

- `/ask` command to get AI responses from GPT-5 nano
- In-game API key management with `/setapikey`, `/removeapikey`, and `/apikeystatus`
- **Client-side only** - works on any server without server installation
- **Persistent API key storage** - your key is saved and persists between game sessions
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

**Note**: Your API key is stored locally on your computer and only you can use it. It's automatically saved and will persist between game sessions.

## How It Works

This is a **client-side** mod that:

- Runs entirely on your local Minecraft client
- Uses the OpenAI Java SDK for reliable API communication
- Stores your API key locally in the Minecraft config folder
- No server setup or permissions required
- **Persistent storage** - your API key is remembered between game sessions

## API Key Storage

Your API key is stored in a file called `mineclanker_config.json` in your Minecraft `config` folder. This file:

- Is created automatically when you set your API key
- Contains only your API key (no other sensitive data)
- Is stored locally on your computer in the proper config location
- Persists between game sessions
- Is automatically loaded when you start the game

**Location**: `.minecraft/config/mineclanker_config.json`

## Configuration

The mod uses these default settings:

- Model: `gpt-5-nano` (latest and cheapest)
- Max tokens: 200 (short responses)
- Temperature: 0.7

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
