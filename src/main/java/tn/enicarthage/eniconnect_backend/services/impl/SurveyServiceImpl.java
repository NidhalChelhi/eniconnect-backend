package tn.enicarthage.eniconnect_backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveyDto;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveySubmissionDto;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.UpdateSurveyDatesDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveyDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveySubmissionDetailsDto;
import tn.enicarthage.eniconnect_backend.entities.*;
import tn.enicarthage.eniconnect_backend.exceptions.ResourceNotFoundException;
import tn.enicarthage.eniconnect_backend.mappers.SurveyMapper;
import tn.enicarthage.eniconnect_backend.repositories.*;
import tn.enicarthage.eniconnect_backend.services.SurveyService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyMapper surveyMapper;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final QuestionTemplateRepository questionTemplateRepository;
    private final SurveyResponseRepository surveyResponseRepository;

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

    // In SurveyServiceImpl
    @Override
    @Transactional
    public void deleteSurvey(Long id) {
        Survey survey = surveyRepository.findByIdWithCourses(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey", "id", id));

        // Clear relationships before deletion
        survey.getTargetCourses().clear();
        surveyRepository.delete(survey);
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


    @Override
    public SurveySubmissionDetailsDto submitSurveyResponse(Long surveyId, CreateSurveySubmissionDto createSurveySubmissionDto, Long studentId) {
        // Validate student exists
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Validate survey exists and is active
        Survey survey = surveyRepository.findByIdWithCourses(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        if (!survey.isActive()) {
            throw new IllegalStateException("Survey is not currently active");
        }

        // Check if student already submitted
        if (surveyResponseRepository.existsByStudentAndSurvey(student, survey)) {
            throw new IllegalStateException("Student already submitted this survey");
        }

        // Validate all courses belong to the survey
        Set<Long> surveyCourseIds = survey.getTargetCourses().stream()
                .map(Course::getId)
                .collect(Collectors.toSet());

        for (CreateSurveySubmissionDto.CourseResponseDto courseResponse : createSurveySubmissionDto.courseResponses()) {
            if (!surveyCourseIds.contains(courseResponse.courseId())) {
                throw new IllegalArgumentException("Course does not belong to this survey");
            }
        }

        // Get all questions in order
        List<QuestionTemplate> questions = questionTemplateRepository.findAllOrdered();
        if (questions.size() != 8) {
            throw new IllegalStateException("Expected exactly 8 questions");
        }

        // Create survey response
        SurveySubmission response = SurveySubmission.builder()
                .student(student)
                .survey(survey)
                .openFeedback(createSurveySubmissionDto.openFeedback())
                .isSubmitted(true)
                .build();

        // Create answers for each course and question
        List<Answer> answers = new ArrayList<>();
        for (CreateSurveySubmissionDto.CourseResponseDto courseResponse : createSurveySubmissionDto.courseResponses()) {
            if (courseResponse.answers().size() != 8) {
                throw new IllegalArgumentException("Each course must have exactly 8 answers");
            }

            for (int i = 0; i < 8; i++) {
                Answer answer = Answer.builder()
                        .submission(response)
                        .question(questions.get(i))
                        .course(courseRepository.findById(courseResponse.courseId())
                                .orElseThrow(() -> new RuntimeException("Course not found")))
                        .rating(courseResponse.answers().get(i))
                        .build();
                answers.add(answer);
            }
        }

        response.setAnswers(answers);
        SurveySubmission savedResponse = surveyResponseRepository.save(response);

        return toResponseDetailsDto(savedResponse);
    }

    @Override
    public SurveySubmissionDetailsDto getStudentResponseForSurvey(Long surveyId, Long studentId) {
        SurveySubmission response = surveyResponseRepository.findByStudentIdAndSurveyId(studentId, surveyId)
                .orElseThrow(() -> new RuntimeException("Response not found"));
        return toResponseDetailsDto(response);
    }

    @Override
    public List<SurveyDto> getActiveSurveysForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        LocalDateTime now = LocalDateTime.now();

        String entryYear = student.getEntrySchoolYear();
        String[] parts = entryYear.split("/");
        int startYear = Integer.parseInt(parts[0]);

        int yearsToAdd = student.getCurrentLevel() - 1;
        List<String> schoolYears = new ArrayList<>();

        for (int i = 0; i <= yearsToAdd; i++) {
            int currentStart = startYear + i;
            schoolYears.add(currentStart + "/" + (currentStart + 1));
        }

        List<Survey> surveys = surveyRepository.findVisibleSurveys(
                student.getSpeciality(),
                student.getCurrentLevel(),
                schoolYears,
                now
        );

        // Filter out surveys that the student has already submitted from survey submissions use existsByStudentAndSurvey
        surveys = surveys.stream()
                .filter(survey -> !surveyResponseRepository.existsByStudentAndSurvey(student, survey))
                .collect(Collectors.toList());


        return surveys.stream()
                .map(surveyMapper::toDto)
                .toList();
    }

    private SurveySubmissionDetailsDto toResponseDetailsDto(SurveySubmission response) {
        List<SurveySubmissionDetailsDto.AnswerDto> answerDtos = response.getAnswers().stream()
                .map(a -> new SurveySubmissionDetailsDto.AnswerDto(
                        a.getQuestion().getId(),
                        a.getCourse().getId(),
                        a.getRating()
                ))
                .toList();

        return new SurveySubmissionDetailsDto(
                response.getId(),
                response.getStudent().getId(),
                response.getSurvey().getId(),
                answerDtos,
                response.getOpenFeedback(),
                response.getSubmittedAt(),
                response.isSubmitted()
        );
    }
}
