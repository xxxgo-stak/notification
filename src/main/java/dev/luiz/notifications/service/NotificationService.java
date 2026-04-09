package dev.luiz.notifications.service;

import dev.luiz.notifications.dtos.NotificationLogResponse;
import dev.luiz.notifications.dtos.NotificationRequest;
import dev.luiz.notifications.dtos.NotificationResponse;
import dev.luiz.notifications.dtos.StatsResponse;
import dev.luiz.notifications.entity.Notification;
import dev.luiz.notifications.entity.Template;
import dev.luiz.notifications.messaging.producer.NotificationProducer;
import dev.luiz.notifications.repository.NotificationLogRepository;
import dev.luiz.notifications.repository.NotificationRepository;
import dev.luiz.notifications.repository.TemplateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final TemplateRepository templateRepository;
    private final NotificationLogRepository notificationLogRepository;
    private final NotificationProducer producer;

    public NotificationService(NotificationRepository notificationRepository,
                               TemplateRepository templateRepository,
                               NotificationLogRepository notificationLogRepository,
                               NotificationProducer producer) {
        this.notificationRepository = notificationRepository;
        this.templateRepository = templateRepository;
        this.notificationLogRepository = notificationLogRepository;
        this.producer = producer;
    }

    public NotificationResponse create(NotificationRequest request) {
        Template template = templateRepository.findById(request.templateId())
                .orElseThrow(() -> new RuntimeException("Template não encontrado"));

        Notification notification = new Notification();
        notification.setTemplate(template);
        notification.setRecipient(request.recipient());
        notification.setChannel(request.channel());
        notification.setPayload(request.payload());
        notification.setStatus("PENDING");
        notification.setAttempts(0);
        notification.setCreatedAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);
        producer.send(saved.getId());

        return new NotificationResponse(saved.getId(), saved.getStatus(), "Notificação enfileirada para envio");
    }

    public NotificationResponse findById(Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
        return new NotificationResponse(n.getId(), n.getStatus(), "Tentativas: " + n.getAttempts());
    }

    public List<NotificationLogResponse> findLogs(Long notificationId) {
        return notificationLogRepository.findByNotificationId(notificationId)
                .stream()
                .map(log -> new NotificationLogResponse(log.getId(), log.getSuccess(), log.getErrorMessage(), log.getAttemptedAt()))
                .toList();
    }

    public StatsResponse getStats() {
        long sent = notificationRepository.countByStatus("SENT");
        long failed = notificationRepository.countByStatus("FAILED");
        long pending = notificationRepository.countByStatus("PENDING");
        return new StatsResponse(sent, failed, pending);
    }

    public List<NotificationResponse> getDeadLetter() {
        return notificationRepository.findByStatus("FAILED")
                .stream()
                .map(n -> new NotificationResponse(n.getId(), n.getStatus(), "Falhou após " + n.getAttempts() + " tentativas"))
                .toList();
    }
}