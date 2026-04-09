package dev.luiz.notifications.messaging.consumer;

import dev.luiz.notifications.config.RabbitMQConfig;
import dev.luiz.notifications.entity.Notification;
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
    private final Map<String, NotificationSender> senders;


    public NotificationConsumer(NotificationRepository notificationRepository,
                                List<NotificationSender> senderList) {
        this.notificationRepository = notificationRepository;
        this.senders = senderList.stream()
                .collect(Collectors.toMap(NotificationSender::getChannel, s -> s));
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificação  não encontrada"));

        NotificationSender sender = senders.get(notification.getChannel());
        if (sender == null) {
            throw new RuntimeException("Canal não suportado:" + notification.getChannel());
        }

        try {
            sender.send(notification);
            notification.setStatus("SENT");
            notification.setSentAt(LocalDateTime.now());
        } catch (Exception e) {
            System.out.println("Erro ao enviar: " + e.getMessage());
            notification.setAttempts(notification.getAttempts() + 1);
            notification.setStatus("FAILED");
        }

        notificationRepository.save(notification);
    }

}
