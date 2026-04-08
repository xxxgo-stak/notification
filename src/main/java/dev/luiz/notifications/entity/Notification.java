package dev.luiz.notifications.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;
    private String recipient;
    private String channel;
    private String status;
    private int attempts;
    @Column(columnDefinition = "jsonb")
    private String payload;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    public Notification() {
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Template getTemplate() {return template;}
    public void setTemplate(Template template) {this.template = template;}

    public String getRecipient() {return recipient;}
    public void setRecipient(String recipient) {this.recipient = recipient;}

    public String getChannel() {return channel;}
    public void setChannel(String channel) {this.channel = channel;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public int getAttempts() {return attempts;}
    public void setAttempts(int attempts) {this.attempts = attempts;}

    public String getPayload() {return payload;}
    public void setPayload(String payload) {this.payload = payload;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getSentAt() {return sentAt;}
    public void setSentAt(LocalDateTime sentAt) {this.sentAt = sentAt;}
}
