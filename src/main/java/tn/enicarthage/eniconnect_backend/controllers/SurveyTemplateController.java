package tn.enicarthage.eniconnect_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.dtos.request_response.CreateTemplateDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.TemplateResponseDTO;
import tn.enicarthage.eniconnect_backend.services.SurveyTemplateService;

import java.util.List;

@RestController
@RequestMapping("/survey-templates")
@RequiredArgsConstructor
public class SurveyTemplateController {
    private final SurveyTemplateService templateService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TemplateResponseDTO> createTemplate(
            @Valid @RequestBody CreateTemplateDTO request) {
        return ResponseEntity.ok(templateService.createTemplate(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateResponseDTO> getTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(templateService.getTemplateById(id));
    }

    @GetMapping
    public ResponseEntity<List<TemplateResponseDTO>> getAllTemplates() {
        return ResponseEntity.ok(templateService.getAllTemplates());
    }
}