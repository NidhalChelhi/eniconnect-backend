// File: src/main/java/tn/enicarthage/eniconnect_backend/services/impl/SurveyServiceImpl.java
package tn.enicarthage.eniconnect_backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.base.AcademicYearDTO;
import tn.enicarthage.eniconnect_backend.dtos.base.CourseDTO;
import tn.enicarthage.eniconnect_backend.dtos.base.SemesterDTO;
import tn.enicarthage.eniconnect_backend.dtos.base.SpecializationDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.CreateSurveyRequestDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.SurveyStatsDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyQuestionDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyTemplateDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyWithQuestionsDTO;
import tn.enicarthage.eniconnect_backend.entities.*;
import tn.enicarthage.eniconnect_backend.exceptions.ResourceNotFoundException;
import tn.enicarthage.eniconnect_backend.exceptions.ResponseSubmissionException;
import tn.enicarthage.eniconnect_backend.exceptions.SurveyClosedException;
import tn.enicarthage.eniconnect_backend.exceptions.ValidationException;
import tn.enicarthage.eniconnect_backend.repositories.*;
import tn.enicarthage.eniconnect_backend.services.SurveyService;
import tn.enicarthage.eniconnect_backend.utils.AcademicUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
    public SurveyDTO createSurvey(CreateSurveyRequestDTO request) {
        SurveyTemplate template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));

        Specialization specialization = specializationRepository.findById(request.getSpecializationId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));

        // Auto-fetch courses for this survey
        List<SemesterCourse> courses = semesterCourseRepository.findBySemesterAndSpecializationAndYear(
                request.getSemesterId(),
                request.getSpecializationId(),
                request.getYearOfStudy());

        if (courses.isEmpty()) {
            throw new ValidationException("No courses found for this specialization, semester and year");
        }

        Survey survey = Survey.builder()
                .surveyTemplate(template)
                .semester(semester)
                .specialization(specialization)
                .yearOfStudy(request.getYearOfStudy())
                .title(request.getTitle())
                .description(request.getDescription())
                .openDate(request.getOpenDate())
                .closeDate(request.getCloseDate())
                .isActive(false)
                .build();

        surveyRepository.save(survey);
        return convertToDTO(survey);
    }


    @Override
    public SurveyDTO getSurveyById(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + id));
        return convertToDTO(survey);
    }

    @Override
    public List<SurveyDTO> getActiveSurveys() {
        try {
            return surveyRepository.findByIsActiveTrue().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseSubmissionException("Failed to retrieve active surveys: " + e.getMessage());
        }
    }

    @Override
    public List<SurveyDTO> getSurveysBySpecializationAndYear(String specializationCode, int yearOfStudy) {
        try {
            Specialization specialization = specializationRepository.findByCode(specializationCode)
                    .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with code: " + specializationCode));

            return surveyRepository.findBySpecializationAndYearOfStudy(specialization, yearOfStudy).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseSubmissionException("Failed to retrieve surveys: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void activateSurvey(Long surveyId) {
        try {
            Survey survey = surveyRepository.findById(surveyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + surveyId));

            if (survey.getOpenDate().isAfter(LocalDateTime.now())) {
                throw new ValidationException("Cannot activate survey before its open date");
            }

            if (survey.getCloseDate().isBefore(LocalDateTime.now())) {
                throw new SurveyClosedException("Cannot activate a survey with past close date");
            }

            survey.setIsActive(true);
            surveyRepository.save(survey);
        } catch (Exception e) {
            throw new ResponseSubmissionException("Failed to activate survey: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void closeSurvey(Long surveyId) {
        try {
            Survey survey = surveyRepository.findById(surveyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + surveyId));

            survey.setIsActive(false);
            surveyRepository.save(survey);
        } catch (Exception e) {
            throw new ResponseSubmissionException("Failed to close survey: " + e.getMessage());
        }
    }

    @Override
    public SurveyStatsDTO getSurveyStats(Long surveyId) {
        try {
            Survey survey = surveyRepository.findById(surveyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + surveyId));

            List<SemesterCourse> courses = semesterCourseRepository
                    .findBySemesterAndSpecializationAndYearOfStudy(
                            survey.getSemester(),
                            survey.getSpecialization(),
                            survey.getYearOfStudy());

            return SurveyStatsDTO.builder()
                    .surveyId(surveyId)
                    .surveyTitle(survey.getTitle())
                    .totalResponses(survey.getResponses().size())
                    .build();
        } catch (Exception e) {
            throw new ResponseSubmissionException("Failed to get survey stats: " + e.getMessage());
        }
    }

    private SurveyDTO convertToDTO(Survey survey) {
        return SurveyDTO.builder()
                .id(survey.getId())
                .template(SurveyTemplateDTO.builder()
                        .id(survey.getSurveyTemplate().getId())
                        .name(survey.getSurveyTemplate().getName())
                        .description(survey.getSurveyTemplate().getDescription())
                        .isSystemDefault(survey.getSurveyTemplate().getIsSystemDefault())
                        .build())
                .semester(SemesterDTO.builder()
                        .id(survey.getSemester().getId())
                        .number(survey.getSemester().getNumber())
                        .startDate(survey.getSemester().getStartDate().toLocalDate())
                        .endDate(survey.getSemester().getEndDate().toLocalDate())
                        .academicYear(AcademicYearDTO.builder()
                                .id(survey.getSemester().getAcademicYear().getId())
                                .name(survey.getSemester().getAcademicYear().getName())
                                .startYear(survey.getSemester().getAcademicYear().getStartYear())
                                .endYear(survey.getSemester().getAcademicYear().getEndYear())
                                .build())
                        .build())
                .specialization(SpecializationDTO.builder()
                        .id(survey.getSpecialization().getId())
                        .code(survey.getSpecialization().getCode())
                        .name(survey.getSpecialization().getName())
                        .build())
                .yearOfStudy(survey.getYearOfStudy())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .openDate(survey.getOpenDate())
                .closeDate(survey.getCloseDate())
                .isActive(survey.getIsActive())
                .build();
    }

    @Override
    @Transactional
    public SurveyWithQuestionsDTO getSurveyWithQuestions(Long id) {
        try {
            Survey survey = surveyRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Survey not found with id: " + id));

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
        } catch (Exception e) {
            throw new ResponseSubmissionException("Failed to get survey with questions: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<SurveyQuestionDTO> getSurveyTemplateQuestions(Long templateId) {
        try {
            SurveyTemplate template = templateRepository.findById(templateId)
                    .orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + templateId));

            return template.getQuestions().stream()
                    .map(this::convertQuestionToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseSubmissionException("Failed to get template questions: " + e.getMessage());
        }
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

        // Get student's current year of study
        int yearOfStudy = AcademicUtils.calculateYearOfStudy(student.getEntryYear());

        // Get current semester
        int currentMonth = LocalDate.now().getMonthValue();
        int semesterNumber = (currentMonth >= 2 && currentMonth <= 6) ? 2 : 1;

        // Find surveys for student's specialization and year of study
        List<Survey> surveys = surveyRepository.findBySpecializationAndYearOfStudy(
                student.getSpecialization(),
                yearOfStudy);

        // Filter for current semester
        return surveys.stream()
                .filter(s -> s.getSemester().getNumber() == semesterNumber)
                .filter(s -> s.getSemester().getAcademicYear().getEndYear() >= LocalDate.now().getYear())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getCoursesForSurvey(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        return semesterCourseRepository
                .findBySemesterAndSpecializationAndYear(
                        survey.getSemester().getId(),
                        survey.getSpecialization().getId(),
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
        return currentYear - entryYear + 1;
    }
}