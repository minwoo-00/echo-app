package com.echo.echo_backend.badge;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

@Component
public class BadgeRuleLoader {

    public String loadBadgeRules(){
        try {
            ClassPathResource resource = new ClassPathResource("prompts/badge_rules.txt");
            return Files.readString(resource.getFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load badge_rules.txt", e);
        }
    }
}
