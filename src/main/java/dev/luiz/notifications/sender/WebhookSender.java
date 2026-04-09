package dev.luiz.notifications.sender;

import dev.luiz.notifications.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class WebhookSender implements NotificationSender {

    @Override
    public void send(Notification notification) {
        System.out.println("Enviando webhook para:" + notification.getRecipient());
    }

    @Override
    public String getChannel() {
        return "WEBHOOK";
    }
}
