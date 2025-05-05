package tn.enicarthage.eniconnect_backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveyDto;
import tn.enicarthage.eniconnect_backend.enums.Speciality;
import tn.enicarthage.eniconnect_backend.exceptions.AlreadyExistsException;
import tn.enicarthage.eniconnect_backend.repositories.CourseRepository;
import tn.enicarthage.eniconnect_backend.services.SurveySeederService;
import tn.enicarthage.eniconnect_backend.services.SurveyService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveySeederServiceImpl implements SurveySeederService {
    private final SurveyService surveyService;
    private final CourseRepository courseRepository;

    @Override
    public void seedAllPossibleSurveys() {
        List<CreateSurveyDto> surveyTemplates = generateAllPossibleSurveyTemplates();
        surveyTemplates.forEach(dto -> {
            try {
                surveyService.createSurvey(dto);
            } catch (AlreadyExistsException e) {
                // Skip if survey already exists
                System.out.println("Survey already exists: " + e.getMessage());
            }
        });
    }

    @Override
    public List<CreateSurveyDto> generateAllPossibleSurveyTemplates() {
        List<CreateSurveyDto> surveys = new ArrayList<>();

        String[] schoolYears = {"2023/2024", "2024/2025", "2025/2026"};

        for (String schoolYear : schoolYears) {
            for (Speciality speciality : Speciality.values()) {
                for (int level = 1; level <= 3; level++) {
                    for (int semester = 1; semester <= 2; semester++) {
                        surveys.add(new CreateSurveyDto(
                                String.format("Survey %s L%d S%d - %s",
                                        speciality, level, semester, schoolYear),
                                speciality,
                                semester,
                                level,
                                schoolYear,
                                null,
                                null
                        ));
                    }
                }
            }
        }

        return surveys;
    }
}