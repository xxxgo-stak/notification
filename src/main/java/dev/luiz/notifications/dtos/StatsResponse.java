package dev.luiz.notifications.dtos;

public record StatsResponse(long sent, long failed, long pending) {}