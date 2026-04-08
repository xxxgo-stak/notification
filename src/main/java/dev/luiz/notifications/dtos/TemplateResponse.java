package dev.luiz.notifications.dtos;

import java.time.LocalDateTime;

public record TemplateResponse(Long id, String name, String subject, String body, String channel, LocalDateTime createdAt) {}