package com.alsritter.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Tool {
    private int toolId;
    private String  toolName;
    private int toolCount;
}
