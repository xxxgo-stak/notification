package dev.luiz.notifications.repository;

import dev.luiz.notifications.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    List<NotificationLog> findByNotificationId(Long notificationId);
}
