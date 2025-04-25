package tn.enicarthage.eniconnect_backend.services;

import jakarta.transaction.Transactional;
import tn.enicarthage.eniconnect_backend.dtos.request_response.CreateTemplateDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.TemplateResponseDTO;

import java.util.List;

public interface SurveyTemplateService {
    TemplateResponseDTO getTemplateById(Long id);

    List<TemplateResponseDTO> getAllTemplates();

    @Transactional
    TemplateResponseDTO createTemplate(CreateTemplateDTO request);
}
