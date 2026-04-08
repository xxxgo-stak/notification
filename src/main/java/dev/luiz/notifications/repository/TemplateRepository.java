package dev.luiz.notifications.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.luiz.notifications.entity.Template;

public interface TemplateRepository extends JpaRepository<Template, Long> {
}
