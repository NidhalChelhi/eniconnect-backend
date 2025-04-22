package tn.enicarthage.eniconnect_backend.services.impl;


import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.base.CourseDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.CreateSurveyRequest;
import tn.enicarthage.eniconnect_backend.dtos.request_response.SurveyStatsDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyQuestionDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyWithQuestionsDTO;
import tn.enicarthage.eniconnect_backend.entities.*;
import tn.enicarthage.eniconnect_backend.exceptions.ResourceNotFoundException;
import tn.enicarthage.eniconnect_backend.repositories.*;
import tn.enicarthage.eniconnect_backend.services.SurveyService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyTemplateRepository templateRepository;
    private final SemesterRepository semesterRepository;
    private final SpecializationRepository specializationRepository;
    private final SemesterCourseRepository semesterCourseRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public SurveyDTO createSurvey(CreateSurveyRequest request) {
        SurveyTemplate template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));

        Specialization specialization = specializationRepository.findById(request.getSpecializationId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));

        if (request.getYearOfStudy() < 1 || request.getYearOfStudy() > 3) {
            throw new ValidationException("Year of study must be between 1 and 3");
        }

        Survey survey = new Survey();
        survey.setSurveyTemplate(template);
        survey.setSemester(semester);
        survey.setSpecialization(specialization);
        survey.setYearOfStudy(request.getYearOfStudy());
        survey.setTitle(request.getTitle());
        survey.setDescription(request.getDescription());
        survey.setOpenDate(request.getOpenDate());
        survey.setCloseDate(request.getCloseDate());
        survey.setIsActive(false);

        surveyRepository.save(survey);
        return convertToDTO(survey);
    }

    @Override
    public SurveyDTO getSurveyById(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));
        return convertToDTO(survey);
    }

    @Override
    public List<SurveyDTO> getActiveSurveys() {
        return surveyRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurveyDTO> getSurveysBySpecializationAndYear(String specializationCode, int yearOfStudy) {
        Optional<Specialization> specOptional = specializationRepository.findByCode(specializationCode);
        Specialization specialization = specOptional.orElseThrow(() ->
                new ResourceNotFoundException("Specialization not found"));

        return surveyRepository.findBySpecializationAndYearOfStudy(specialization, yearOfStudy).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void activateSurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        if (survey.getOpenDate().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Cannot activate survey before open date");
        }

        if (survey.getCloseDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Cannot activate a survey with past close date");
        }

        survey.setIsActive(true);
        surveyRepository.save(survey);
    }

    @Override
    @Transactional
    public void closeSurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        survey.setIsActive(false);
        surveyRepository.save(survey);
    }

    @Override
    public SurveyStatsDTO getSurveyStats(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        List<SemesterCourse> courses = semesterCourseRepository
                .findBySemesterAndSpecializationAndYearOfStudy(
                        survey.getSemester(),
                        survey.getSpecialization(),
                        survey.getYearOfStudy());

        // Implementation of statistics calculation would go here
        // This is a simplified version
        return SurveyStatsDTO.builder()
                .surveyId(surveyId)
                .surveyTitle(survey.getTitle())
                .totalResponses(survey.getResponses().size())
                .build();
    }

    private SurveyDTO convertToDTO(Survey survey) {
        return SurveyDTO.builder()
                .id(survey.getId())
                .title(survey.getTitle()    )
                .description(survey.getDescription())
                .yearOfStudy(survey.getYearOfStudy())
                .openDate(survey.getOpenDate())
                .closeDate(survey.getCloseDate())
                .isActive(survey.getIsActive())
                .build();
    }


    @Override
    public SurveyWithQuestionsDTO getSurveyWithQuestions(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        List<SemesterCourse> courses = semesterCourseRepository
                .findBySemesterAndSpecializationAndYearOfStudy(
                        survey.getSemester(),
                        survey.getSpecialization(),
                        survey.getYearOfStudy());

        return SurveyWithQuestionsDTO.builder()
                .id(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .yearOfStudy(survey.getYearOfStudy())
                .openDate(survey.getOpenDate())
                .closeDate(survey.getCloseDate())
                .isActive(survey.getIsActive())
                .questions(survey.getSurveyTemplate().getQuestions().stream()
                        .map(this::convertQuestionToDTO)
                        .collect(Collectors.toList()))
                .courses(courses.stream()
                        .map(sc -> CourseDTO.builder()
                                .id(sc.getCourse().getId())
                                .name(sc.getCourse().getName())
                                .code(sc.getCourse().getCode())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<SurveyQuestionDTO> getSurveyTemplateQuestions(Long templateId) {
        SurveyTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        return template.getQuestions().stream()
                .map(this::convertQuestionToDTO)
                .collect(Collectors.toList());
    }

    private SurveyQuestionDTO convertQuestionToDTO(SurveyQuestion question) {
        return SurveyQuestionDTO.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .questionType(question.getQuestionType())
                .questionOrder(question.getQuestionOrder())
                .options(question.getOptions() != null ?
                        Arrays.asList(question.getOptions().split(",")) : List.of())
                .build();
    }

    @Override
    public List<SurveyDTO> getSurveysForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        return surveyRepository.findBySpecializationAndYearOfStudy(
                        student.getSpecialization(),
                        calculateYearOfStudy(student.getEntryYear()))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getCoursesForSurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        return semesterCourseRepository
                .findBySemesterAndSpecializationAndYearOfStudy(
                        survey.getSemester(),
                        survey.getSpecialization(),
                        survey.getYearOfStudy())
                .stream()
                .map(sc -> CourseDTO.builder()
                        .id(sc.getCourse().getId())
                        .name(sc.getCourse().getName())
                        .code(sc.getCourse().getCode())
                        .build())
                .collect(Collectors.toList());
    }

    private int calculateYearOfStudy(int entryYear) {
        int currentYear = java.time.Year.now().getValue();
        return currentYear - entryYear + 1; // +1 because first year is year 1
    }
}