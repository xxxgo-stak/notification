package dev.luiz.notifications.service;

import dev.luiz.notifications.dtos.TemplateRequest;
import dev.luiz.notifications.dtos.TemplateResponse;
import dev.luiz.notifications.entity.Template;
import org.springframework.stereotype.Service;
import dev.luiz.notifications.repository.TemplateRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TemplateService {
    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public TemplateResponse create(TemplateRequest request) {
        Template template = new Template();
        template.setName(request.name());
        template.setSubject(request.subject());
        template.setBody(request.body());
        template.setChannel(request.channel());
        template.setCreatedAt(LocalDateTime.now());

        Template saved = templateRepository.save(template);

        return new TemplateResponse(saved.getId(), saved.getName(), saved.getSubject(), saved.getBody(), saved.getChannel(), saved.getCreatedAt());
    }

    public List<TemplateResponse> findAll(){
        return templateRepository.findAll()
                .stream()
                .map(t -> new TemplateResponse(t.getId(), t.getName(), t.getSubject(), t.getBody(), t.getChannel(), t.getCreatedAt()))
                .toList();
    }

    public TemplateResponse findById(Long id) {
        Template t = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template não encontrado"));
        return new TemplateResponse(t.getId(), t.getName(), t.getSubject(), t.getBody(), t.getChannel(), t.getCreatedAt());
    }

    public TemplateResponse update(Long id, TemplateRequest request) {
        Template t = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template não encontrado"));
        t.setName(request.name());
        t.setSubject(request.subject());
        t.setBody(request.body());
        t.setChannel(request.channel());
        Template saved = templateRepository.save(t);
        return new TemplateResponse(saved.getId(), saved.getName(), saved.getSubject(), saved.getBody(), saved.getChannel(), saved.getCreatedAt());
    }

    public void delete(Long id) {
        templateRepository.deleteById(id);
    }
}
