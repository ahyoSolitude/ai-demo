package com.example.aidemo.tools;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ToolParameter {
    private String name;
    private String type;
    private String description;
    private boolean required;
}