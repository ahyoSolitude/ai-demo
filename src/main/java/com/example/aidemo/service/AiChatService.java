package com.example.aidemo.service;

import com.example.aidemo.model.ChatMessage;
import com.example.aidemo.model.ChatRequest;
import com.example.aidemo.model.ChatResponse;
import com.example.aidemo.tools.ToolRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiChatService {

    private final ToolRegistry toolRegistry;
    private final Map<String, List<ChatMessage>> conversationHistory = new HashMap<>();
    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;
    
    @Value("${spring.ai.openai.chat.options.model:gpt-3.5-turbo}")
    private String model;
    
    @Value("${spring.ai.openai.chat.options.temperature:0.7}")
    private float temperature;

    @Autowired
    public AiChatService(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .build();
    }

    public ChatResponse chat(ChatRequest request) {
        String sessionId = request.getSessionId();
        String userMessage = request.getMessage();
        
        // Get or create conversation history for this session
        List<ChatMessage> messages = conversationHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());
        
        // Add system message if this is a new conversation
        if (messages.isEmpty()) {
            String systemPrompt = """
                You are a helpful AI assistant that can use tools to answer user questions.
                You have access to the following tools:
                - getWeather: Get the current weather for a specific location
                - add, subtract, multiply, divide: Perform basic arithmetic calculations
                - search: Search for information on a specific topic
                
                When a user asks a question that requires using these tools, use them to provide accurate information.
                Always be helpful, concise, and accurate.
                """;
            
            messages.add(ChatMessage.system(systemPrompt));
        }
        
        // Add the new user message
        messages.add(ChatMessage.user(userMessage));
        
        // Create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", messages);
        requestBody.put("temperature", temperature);
        requestBody.put("tools", toolRegistry.getToolDefinitions());
        requestBody.put("tool_choice", "auto");
        
        try {
            // Call OpenAI API
            Map<String, Object> response = restClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + apiKey)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);
            
            // Process the response
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> choice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) choice.get("message");
            String content = (String) message.get("content");
            
            // Check if there are tool calls to process
            List<Map<String, Object>> toolCalls = (List<Map<String, Object>>) message.get("tool_calls");
            
            if (toolCalls != null && !toolCalls.isEmpty()) {
                // Add assistant message with tool calls
                ChatMessage assistantMessage = ChatMessage.assistant(content != null ? content : "I need to use a tool to answer your question.");
                messages.add(assistantMessage);
                
                // Process each tool call
                for (Map<String, Object> toolCall : toolCalls) {
                    Map<String, Object> function = (Map<String, Object>) toolCall.get("function");
                    String toolName = (String) function.get("name");
                    String argsJson = (String) function.get("arguments");
                    
                    // Parse arguments as Map
                    Map<String, Object> args;
                    try {
                        args = objectMapper.readValue(argsJson, Map.class);
                    } catch (JsonProcessingException e) {
                        args = new HashMap<>();
                    }
                    
                    // Execute the tool
                    String toolResult = toolRegistry.executeToolByName(toolName, args);
                    
                    // Add tool result to the conversation
                    messages.add(ChatMessage.user("Tool " + toolName + " result: " + toolResult));
                }
                
                // Get a final response after tool execution
                return chat(new ChatRequest(sessionId, "Based on the tool results, please provide your final answer."));
            }
            
            // Add the assistant's response to the conversation history
            ChatMessage assistantMessage = ChatMessage.assistant(content);
            messages.add(assistantMessage);
            
            // Convert to our response format
            return new ChatResponse(content);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ChatResponse("Error: " + e.getMessage());
        }
    }
    
    public void clearConversation(String sessionId) {
        conversationHistory.remove(sessionId);
    }
}