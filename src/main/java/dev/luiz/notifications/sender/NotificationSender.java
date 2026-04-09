package dev.luiz.notifications.sender;

import dev.luiz.notifications.entity.Notification;

public interface NotificationSender {
    void send(Notification notification);
    String getChannel();
}
