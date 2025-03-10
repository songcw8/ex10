package org.example.model;

import javax.naming.Name;
import java.lang.management.PlatformLoggingMXBean;

public enum ModelType {
    GROQ_LLAMA("llama-3.3-70b-versatile", ModelPlatform.GROQ),
    TOGETHER_LLAMA("meta-llama/llama-3.3-70b-versatile", ModelPlatform.TOGETHER_LLAMA),;

    ModelType(String name, ModelPlatform platform){
        this.name = name;
        this.platform = platform;
    }

    final String name;
    final ModelPlatform platform;
}
