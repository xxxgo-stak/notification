package dev.luiz.notifications.dtos;

import jakarta.validation.constraints.NotBlank;

public record TemplateRequest(
        @NotBlank(message = "Nome é obrigatório") String name,
        @NotBlank(message = "Assunto é obrigatório") String subject,
        @NotBlank(message = "Corpo é obrigatório") String body,
        @NotBlank(message = "Canal é obrigatório") String channel
) {}
