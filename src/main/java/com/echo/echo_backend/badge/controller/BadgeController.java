package com.echo.echo_backend.badge.controller;

import com.echo.echo_backend.badge.service.BadgeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/badge")
@RequiredArgsConstructor
public class BadgeController {
    private final BadgeService badgeService;

    @Operation(summary = "Test badge genarate")
    @GetMapping("/generate")
    public void generateBadge(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        badgeService.generateBadge(userId);
    }
}
