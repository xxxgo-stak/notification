package dev.luiz.notifications.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest(
        @NotNull(message = "Template ID é obrigatório") Long templateId,
        @NotBlank(message = "Destinatário é obrigatório") String recipient,
        @NotBlank(message = "Canal é obrigatório") String channel,
        String payload
) {}