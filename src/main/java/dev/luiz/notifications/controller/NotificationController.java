package dev.luiz.notifications.controller;

import dev.luiz.notifications.dtos.NotificationLogResponse;
import dev.luiz.notifications.dtos.NotificationRequest;
import dev.luiz.notifications.dtos.NotificationResponse;
import dev.luiz.notifications.dtos.StatsResponse;
import dev.luiz.notifications.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody NotificationRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(notificationService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.findById(id));
    }

    @GetMapping("/{id}/logs")
    public ResponseEntity<List<NotificationLogResponse>> findLogs(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.findLogs(id));
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(notificationService.getStats());
    }

    @GetMapping("/dead-letter")
    public ResponseEntity<List<NotificationResponse>> getDeadLetter() {
        return ResponseEntity.ok(notificationService.getDeadLetter());
    }
}