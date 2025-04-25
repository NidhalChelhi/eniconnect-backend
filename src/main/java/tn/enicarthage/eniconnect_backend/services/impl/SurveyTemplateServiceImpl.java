package tn.enicarthage.eniconnect_backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.base.TemplateQuestionDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.CreateTemplateDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.TemplateResponseDTO;
import tn.enicarthage.eniconnect_backend.entities.SurveyQuestion;
import tn.enicarthage.eniconnect_backend.entities.SurveyTemplate;
import tn.enicarthage.eniconnect_backend.exceptions.ResourceNotFoundException;
import tn.enicarthage.eniconnect_backend.repositories.SurveyQuestionRepository;
import tn.enicarthage.eniconnect_backend.repositories.SurveyTemplateRepository;
import tn.enicarthage.eniconnect_backend.services.SurveyTemplateService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyTemplateServiceImpl implements SurveyTemplateService {
    private final SurveyTemplateRepository templateRepository;
    private final SurveyQuestionRepository questionRepository;


    @Override
    public TemplateResponseDTO getTemplateById(Long id) {
        SurveyTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
        return convertToDTO(template);
    }

    @Override
    public List<TemplateResponseDTO> getAllTemplates() {
        return templateRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public TemplateResponseDTO createTemplate(CreateTemplateDTO request) {
        SurveyTemplate template = SurveyTemplate.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isSystemDefault(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        template = templateRepository.save(template);

        SurveyTemplate finalTemplate = template;
        List<SurveyQuestion> questions = request.getQuestions().stream()
                .map(q -> SurveyQuestion.builder()
                        .surveyTemplate(finalTemplate)
                        .questionText(q.getQuestionText())
                        .questionType(q.getQuestionType())
                        .questionOrder(q.getQuestionOrder())
                        .options(q.getOptions() != null ? String.join(",", q.getOptions()) : null)
                        .build())
                .collect(Collectors.toList());

        questionRepository.saveAll(questions);
        template.setQuestions(questions);

        return convertToDTO(template);
    }

    // ... other methods ...

    private TemplateResponseDTO convertToDTO(SurveyTemplate template) {
        return TemplateResponseDTO.builder()
                .id(template.getId())
                .name(template.getName())
                .description(template.getDescription())
                .questions(template.getQuestions().stream()
                        .map(q -> TemplateQuestionDTO.builder()
                                .id(q.getId())
                                .questionText(q.getQuestionText())
                                .questionType(q.getQuestionType())
                                .options(q.getOptions() != null ?
                                        Arrays.asList(q.getOptions().split(",")) : List.of())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}