package dev.luiz.notifications.messaging.consumer;

import dev.luiz.notifications.config.RabbitMQConfig;
import dev.luiz.notifications.entity.Notification;
import dev.luiz.notifications.entity.NotificationLog;
import dev.luiz.notifications.repository.NotificationLogRepository;
import dev.luiz.notifications.repository.NotificationRepository;
import dev.luiz.notifications.sender.NotificationSender;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final NotificationLogRepository notificationLogRepository;
    private final Map<String, NotificationSender> senders;

    public NotificationConsumer(NotificationRepository notificationRepository,
                                NotificationLogRepository notificationLogRepository,
                                List<NotificationSender> senderList) {
        this.notificationRepository = notificationRepository;
        this.notificationLogRepository = notificationLogRepository;
        this.senders = senderList.stream()
                .collect(Collectors.toMap(NotificationSender::getChannel, s -> s));
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));

        NotificationSender sender = senders.get(notification.getChannel());
        if (sender == null) {
            throw new RuntimeException("Canal não suportado: " + notification.getChannel());
        }

        try {
            sender.send(notification);
            notification.setStatus("SENT");
            notification.setSentAt(LocalDateTime.now());
            saveLog(notification, true, null);
        } catch (Exception e) {
            notification.setAttempts(notification.getAttempts() + 1);
            saveLog(notification, false, e.getMessage());

            if (notification.getAttempts() >= 3) {
                notification.setStatus("FAILED");
            } else {
                notification.setStatus("PENDING");
                throw new RuntimeException("Retry: " + e.getMessage());
            }
        }

        notificationRepository.save(notification);
    }

    private void saveLog(Notification notification, boolean success, String errorMessage) {
        NotificationLog log = new NotificationLog();
        log.setNotification(notification);
        log.setSuccess(success);
        log.setErrorMessage(errorMessage);
        log.setAttemptedAt(LocalDateTime.now());
        notificationLogRepository.save(log);
    }
}