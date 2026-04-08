package dev.luiz.notifications.repository;

import dev.luiz.notifications.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
