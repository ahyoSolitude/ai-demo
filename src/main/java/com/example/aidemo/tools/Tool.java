package com.example.aidemo.tools;

import com.example.aidemo.model.ChatMessage;

import java.util.List;
import java.util.Map;

public interface Tool {
    String getName();
    String getDescription();
    List<ToolParameter> getParameters();
    String invoke(ChatMessage userMessage, Map<String, Object> parameters);
}