package com.example.aidemo.controller;

import com.example.aidemo.model.ChatRequest;
import com.example.aidemo.model.ChatResponse;
import com.example.aidemo.service.AiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final AiChatService aiChatService;

    @Autowired
    public ChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return aiChatService.chat(request);
    }

    @DeleteMapping("/{sessionId}")
    public void clearConversation(@PathVariable String sessionId) {
        aiChatService.clearConversation(sessionId);
    }
}