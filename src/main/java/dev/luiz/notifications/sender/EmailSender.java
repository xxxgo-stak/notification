package dev.luiz.notifications.sender;

import dev.luiz.notifications.entity.Notification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender implements NotificationSender {

    private final JavaMailSender mailSender;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(Notification notification) {
        String body = notification.getTemplate().getBody();

        // Substitui placeholders com valores do payload
        if (notification.getPayload() != null) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                java.util.Map<String, String> data = mapper.readValue(notification.getPayload(), java.util.Map.class);
                for (var entry : data.entrySet()) {
                    body = body.replace("{{" + entry.getKey() + "}}", entry.getValue());
                }
            } catch (Exception e) {
                // ignora erro de parse
            }
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getRecipient());
        message.setSubject(notification.getTemplate().getSubject());
        message.setText(body);
        message.setFrom("noreply@notifications.dev");

        mailSender.send(message);
    }

    @Override
    public String getChannel() {
        return "EMAIL";
    }
}
