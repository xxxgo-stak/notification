package dev.luiz.notifications.repository;

import dev.luiz.notifications.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStatus(String status);
    long countByStatus(String status);
}
