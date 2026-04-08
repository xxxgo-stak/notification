package dev.luiz.notifications.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_logs")
public class NotificationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;
    private Boolean success;
    @Column(name = "error_message")
    private String errorMessage;
    @Column(name = "attempted_at")
    private LocalDateTime attemptedAt;

    public NotificationLog() {
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Notification getNotification() {return notification;}
    public void setNotification(Notification notification) {this.notification = notification;}

    public Boolean getSuccess() {return success;}
    public void setSuccess(Boolean success) {this.success = success;}

    public String getErrorMessage() {return errorMessage;}
    public void setErrorMessage(String errorMessage) {this.errorMessage = errorMessage;}

    public LocalDateTime getAttemptedAt() {return attemptedAt;}
    public void setAttemptedAt(LocalDateTime attemptedAt) {this.attemptedAt = attemptedAt;}
}
