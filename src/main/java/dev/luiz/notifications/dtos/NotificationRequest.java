package dev.luiz.notifications.dtos;

public record NotificationRequest(Long templateId, String recipient, String channel, String payload) {
}
