package com.example.aidemo.tools;

import com.example.aidemo.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ToolRegistry {

    private final Map<String, Tool> tools = new HashMap<>();

    @Autowired
    public ToolRegistry(List<Tool> toolList) {
        for (Tool tool : toolList) {
            tools.put(tool.getName(), tool);
        }
    }

    public List<Map<String, Object>> getToolDefinitions() {
        List<Map<String, Object>> toolDefinitions = new ArrayList<>();
        
        for (Tool tool : tools.values()) {
            Map<String, Object> toolDef = new HashMap<>();
            toolDef.put("type", "function");
            
            Map<String, Object> function = new HashMap<>();
            function.put("name", tool.getName());
            function.put("description", tool.getDescription());
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("type", "object");
            
            Map<String, Object> properties = new HashMap<>();
            Map<String, List<String>> required = new HashMap<>();
            List<String> requiredParams = new ArrayList<>();
            
            for (ToolParameter param : tool.getParameters()) {
                Map<String, Object> paramDef = new HashMap<>();
                paramDef.put("type", param.getType());
                paramDef.put("description", param.getDescription());
                
                properties.put(param.getName(), paramDef);
                
                if (param.isRequired()) {
                    requiredParams.add(param.getName());
                }
            }
            
            parameters.put("properties", properties);
            parameters.put("required", requiredParams);
            
            function.put("parameters", parameters);
            toolDef.put("function", function);
            
            toolDefinitions.add(toolDef);
        }
        
        return toolDefinitions;
    }

    public String executeToolByName(String toolName, Map<String, Object> parameters) {
        Tool tool = tools.get(toolName);
        if (tool == null) {
            return "Error: Tool '" + toolName + "' not found";
        }
        
        try {
            // Create a dummy user message since we don't have the original in this context
            ChatMessage dummyMessage = ChatMessage.user("Execute tool: " + toolName);
            return tool.invoke(dummyMessage, parameters);
        } catch (Exception e) {
            return "Error executing tool '" + toolName + "': " + e.getMessage();
        }
    }
}