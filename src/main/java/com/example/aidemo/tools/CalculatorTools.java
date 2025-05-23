package com.example.aidemo.tools;

import com.example.aidemo.model.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CalculatorTools {

    @Component
    public static class AddTool implements Tool {
        @Override
        public String getName() {
            return "add";
        }

        @Override
        public String getDescription() {
            return "Add two numbers together";
        }

        @Override
        public List<ToolParameter> getParameters() {
            return List.of(
                new ToolParameter("a", "number", "First number", true),
                new ToolParameter("b", "number", "Second number", true)
            );
        }

        @Override
        public String invoke(ChatMessage userMessage, Map<String, Object> parameters) {
            try {
                double a = Double.parseDouble(parameters.get("a").toString());
                double b = Double.parseDouble(parameters.get("b").toString());
                double result = a + b;
                return String.format("%.2f + %.2f = %.2f", a, b, result);
            } catch (Exception e) {
                return "Error performing addition: " + e.getMessage();
            }
        }
    }

    @Component
    public static class SubtractTool implements Tool {
        @Override
        public String getName() {
            return "subtract";
        }

        @Override
        public String getDescription() {
            return "Subtract the second number from the first";
        }

        @Override
        public List<ToolParameter> getParameters() {
            return List.of(
                new ToolParameter("a", "number", "First number", true),
                new ToolParameter("b", "number", "Second number", true)
            );
        }

        @Override
        public String invoke(ChatMessage userMessage, Map<String, Object> parameters) {
            try {
                double a = Double.parseDouble(parameters.get("a").toString());
                double b = Double.parseDouble(parameters.get("b").toString());
                double result = a - b;
                return String.format("%.2f - %.2f = %.2f", a, b, result);
            } catch (Exception e) {
                return "Error performing subtraction: " + e.getMessage();
            }
        }
    }

    @Component
    public static class MultiplyTool implements Tool {
        @Override
        public String getName() {
            return "multiply";
        }

        @Override
        public String getDescription() {
            return "Multiply two numbers";
        }

        @Override
        public List<ToolParameter> getParameters() {
            return List.of(
                new ToolParameter("a", "number", "First number", true),
                new ToolParameter("b", "number", "Second number", true)
            );
        }

        @Override
        public String invoke(ChatMessage userMessage, Map<String, Object> parameters) {
            try {
                double a = Double.parseDouble(parameters.get("a").toString());
                double b = Double.parseDouble(parameters.get("b").toString());
                double result = a * b;
                return String.format("%.2f * %.2f = %.2f", a, b, result);
            } catch (Exception e) {
                return "Error performing multiplication: " + e.getMessage();
            }
        }
    }

    @Component
    public static class DivideTool implements Tool {
        @Override
        public String getName() {
            return "divide";
        }

        @Override
        public String getDescription() {
            return "Divide the first number by the second";
        }

        @Override
        public List<ToolParameter> getParameters() {
            return List.of(
                new ToolParameter("a", "number", "First number (dividend)", true),
                new ToolParameter("b", "number", "Second number (divisor)", true)
            );
        }

        @Override
        public String invoke(ChatMessage userMessage, Map<String, Object> parameters) {
            try {
                double a = Double.parseDouble(parameters.get("a").toString());
                double b = Double.parseDouble(parameters.get("b").toString());
                
                if (b == 0) {
                    return "Error: Cannot divide by zero";
                }
                
                double result = a / b;
                return String.format("%.2f / %.2f = %.2f", a, b, result);
            } catch (Exception e) {
                return "Error performing division: " + e.getMessage();
            }
        }
    }
}