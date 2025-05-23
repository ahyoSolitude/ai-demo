package com.example.aidemo.tools;

import com.example.aidemo.model.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SearchTools implements Tool {

    private final Map<String, String> knowledgeBase = new HashMap<>();

    public SearchTools() {
        // Initialize with some sample data
        knowledgeBase.put("spring ai", "Spring AI is a framework that provides abstractions for using AI models from different providers. " +
                "It supports various AI models including OpenAI, Azure OpenAI, Ollama, and more. " +
                "Spring AI makes it easy to integrate AI capabilities into Spring applications.");
        
        knowledgeBase.put("spring boot", "Spring Boot is an open-source Java-based framework used to create stand-alone, production-grade Spring-based applications. " +
                "It provides a simpler and faster way to set up, configure, and run both simple and web-based applications.");
        
        knowledgeBase.put("java", "Java is a high-level, class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible. " +
                "It is a general-purpose programming language intended to let application developers write once, run anywhere (WORA).");
        
        knowledgeBase.put("artificial intelligence", "Artificial intelligence (AI) is intelligence demonstrated by machines, as opposed to natural intelligence displayed by animals including humans. " +
                "AI applications include advanced web search engines, recommendation systems, voice assistants, and self-driving cars.");
        
        knowledgeBase.put("machine learning", "Machine learning is a subset of artificial intelligence that provides systems the ability to automatically learn and improve from experience without being explicitly programmed. " +
                "It focuses on the development of computer programs that can access data and use it to learn for themselves.");
    }

    @Override
    public String getName() {
        return "search";
    }

    @Override
    public String getDescription() {
        return "Search for information on a specific topic";
    }

    @Override
    public List<ToolParameter> getParameters() {
        return List.of(
            new ToolParameter("query", "string", "The search query", true)
        );
    }

    @Override
    public String invoke(ChatMessage userMessage, Map<String, Object> parameters) {
        try {
            String query = (String) parameters.get("query");
            String result = findInKnowledgeBase(query.toLowerCase());
            
            if (result != null) {
                return "Search results for '" + query + "':\n\n" + result;
            } else {
                return "No specific information found for '" + query + "'. Please try a different search term.";
            }
        } catch (Exception e) {
            return "Error performing search: " + e.getMessage();
        }
    }
    
    private String findInKnowledgeBase(String query) {
        // Direct match
        if (knowledgeBase.containsKey(query)) {
            return knowledgeBase.get(query);
        }
        
        // Partial match
        for (Map.Entry<String, String> entry : knowledgeBase.entrySet()) {
            if (entry.getKey().contains(query) || query.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        // No match found
        return null;
    }
}