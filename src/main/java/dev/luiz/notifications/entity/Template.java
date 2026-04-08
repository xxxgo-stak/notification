package dev.luiz.notifications.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "templates")
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String subject;
    private String body;
    private String channel;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Template() {
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getSubject() {return subject;}
    public void setSubject(String subject) {this.subject = subject;}

    public String getBody() {return body;}
    public void setBody(String body) {this.body = body;}

    public String getChannel() {return channel;}
    public void setChannel(String channel) {this.channel = channel;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}