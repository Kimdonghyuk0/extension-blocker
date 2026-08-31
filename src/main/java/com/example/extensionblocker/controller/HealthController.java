package com.example.extensionblocker.controller;

import com.example.extensionblocker.repository.BlockedExtensionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Skeleton endpoint to confirm the app + DB + Flyway are wired up.
 * Real feature endpoints (policy CRUD, upload) are added per tech spec.
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    private final BlockedExtensionRepository repository;

    public HealthController(BlockedExtensionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "seededExtensions", repository.count()
        );
    }
}
