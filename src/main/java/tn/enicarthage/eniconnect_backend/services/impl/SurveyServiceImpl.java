package tn.enicarthage.eniconnect_backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.*;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveyDto;
import tn.enicarthage.eniconnect_backend.entities.*;
import tn.enicarthage.eniconnect_backend.mappers.SurveyMapper;
import tn.enicarthage.eniconnect_backend.repositories.*;
import tn.enicarthage.eniconnect_backend.services.SurveyService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyMapper surveyMapper;
    private final CourseRepository courseRepository;

    @Override
    public SurveyDto getSurveyById(Long id) {
        return surveyRepository.findById(id)
                .map(surveyMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
    }

    @Override
    public List<SurveyDto> getAllSurveys() {
        return surveyRepository.findAll().stream()
                .map(surveyMapper::toDto)
                .toList();
    }

    @Override
    public Page<SurveyDto> getAllSurveys(Pageable pageable) {
        return surveyRepository.findAll(pageable)
                .map(surveyMapper::toDto);
    }

    @Override
    public SurveyDto createSurvey(CreateSurveyDto dto) {
        // Check for duplicate surveys
        if (surveyRepository.existsBySpecialityAndLevelAndSemesterAndSchoolYear(
                dto.speciality(), dto.level(), dto.semester(), dto.schoolYear())) {
            throw new IllegalArgumentException("Survey already exists for these criteria");
        }

        // Get related courses
        Set<Course> courses = new HashSet<>(courseRepository.findBySpecialityAndLevelAndSemester(
                dto.speciality(), dto.level(), dto.semester()));

        Survey survey = surveyMapper.toEntity(dto, courses);
        return surveyMapper.toDto(surveyRepository.save(survey));
    }

    @Override
    public void deleteSurvey(Long id) {
        surveyRepository.deleteById(id);
    }

    @Override
    public SurveyDto publishSurvey(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        survey.publish();
        return surveyMapper.toDto(surveyRepository.save(survey));
    }

    @Override
    public SurveyDto unpublishSurvey(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        survey.unpublish();
        return surveyMapper.toDto(surveyRepository.save(survey));
    }

    @Override
    public SurveyDto updateSurveyDates(Long surveyId, UpdateSurveyDatesDto dto) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        // For published surveys, we only allow extending the close date
        if (survey.isPublished()) {
            if (dto.newOpenDate() != null) {
                throw new IllegalArgumentException("Cannot change open date for published survey");
            }

            if (dto.newCloseDate() != null) {
                LocalDateTime now = LocalDateTime.now();
                if (dto.newCloseDate().isBefore(now)) {
                    throw new IllegalArgumentException("Close date must be in the future");
                }
                survey.setCloseDate(dto.newCloseDate());
            }
        }
        // For unpublished surveys, allow full date changes
        else {
            survey.setOpenDate(dto.newOpenDate());
            survey.setCloseDate(dto.newCloseDate());
        }

        return surveyMapper.toDto(surveyRepository.save(survey));
    }
}