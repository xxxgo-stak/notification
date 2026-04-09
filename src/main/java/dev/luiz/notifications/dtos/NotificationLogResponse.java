package dev.luiz.notifications.dtos;

import java.time.LocalDateTime;

public record NotificationLogResponse(Long id, boolean success, String errorMessage, LocalDateTime attemptedAt) {}