package dev.luiz.notifications.service;

import dev.luiz.notifications.dtos.NotificationRequest;
import dev.luiz.notifications.dtos.NotificationResponse;
import dev.luiz.notifications.entity.Notification;
import dev.luiz.notifications.entity.Template;
import dev.luiz.notifications.messaging.producer.NotificationProducer;
import dev.luiz.notifications.repository.NotificationRepository;
import dev.luiz.notifications.repository.TemplateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final TemplateRepository templateRepository;
    private final NotificationProducer producer;

    public NotificationService(NotificationRepository notificationRepository,
                               TemplateRepository templateRepository,
                               NotificationProducer producer) {
        this.notificationRepository = notificationRepository;
        this.templateRepository = templateRepository;
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

        return new NotificationResponse(saved.getId(), saved.getStatus(), "Notificação na fila de envio");
    }

}
