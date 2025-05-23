# Spring AI Demo - Multi-round Tool Calls

This application demonstrates how to use Spring AI to create an AI assistant that can solve complex problems through multiple rounds of tool calls.

## Features

- Integration with OpenAI's GPT models via Spring AI
- Custom tool implementations for:
  - Weather information retrieval
  - Basic calculator operations
  - Information search
- Web interface for interacting with the AI assistant
- Support for multi-round conversations with tool calls

## Prerequisites

- Java 17 or higher
- Maven
- OpenAI API key

## Running the Application

1. Set your OpenAI API key as an environment variable:
   ```
   export OPENAI_API_KEY=your_api_key_here
   ```

2. Build the application:
   ```
   mvn clean package
   ```

3. Run the application:
   ```
   java -jar target/ai-demo-0.0.1-SNAPSHOT.jar
   ```

4. Open your browser and navigate to `http://localhost:12000`

## Example Queries

Try asking the AI assistant:

- "What's the weather like in Tokyo?"
- "Calculate 1234 divided by 56"
- "Tell me about Spring AI"
- "I'm planning a trip to Paris. What's the weather there and what's 250 euros in dollars if the exchange rate is 1.08?"

## How It Works

1. The application uses Spring AI to integrate with OpenAI's models
2. Custom tools are implemented as Spring components
3. When a user asks a question, the AI determines if it needs to use tools to answer
4. If tools are needed, the AI makes tool calls and incorporates the results into its response
5. The conversation history is maintained for context in follow-up questions

## Architecture

- `AiDemoApplication.java`: Main Spring Boot application
- `tools/`: Custom tool implementations
- `service/AiChatService.java`: Service for handling AI chat functionality
- `controller/`: REST controllers for the API and web interface
- `model/`: Data models for requests and responses
