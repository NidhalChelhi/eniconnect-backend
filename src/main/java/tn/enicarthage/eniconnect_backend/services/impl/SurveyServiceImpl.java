package tn.enicarthage.eniconnect_backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveyDto;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveySubmissionDto;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.SurveyFilterParams;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.UpdateSurveyDatesDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveyDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveyStatsDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveySubmissionDetailsDto;
import tn.enicarthage.eniconnect_backend.entities.*;
import tn.enicarthage.eniconnect_backend.exceptions.AlreadyExistsException;
import tn.enicarthage.eniconnect_backend.exceptions.InvalidDataException;
import tn.enicarthage.eniconnect_backend.exceptions.OperationNotAllowedException;
import tn.enicarthage.eniconnect_backend.exceptions.ResourceNotFoundException;
import tn.enicarthage.eniconnect_backend.mappers.SurveyMapper;
import tn.enicarthage.eniconnect_backend.repositories.*;
import tn.enicarthage.eniconnect_backend.services.SurveyService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
                .orElseThrow(() -> new ResourceNotFoundException("Survey", "id", id));
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
        if (surveyRepository.existsBySpecialityAndLevelAndSemesterAndSchoolYear(
                dto.speciality(), dto.level(), dto.semester(), dto.schoolYear())) {
            throw new AlreadyExistsException("Survey", "criteria",
                    String.format("%s-%s-%s-%s", dto.speciality(), dto.level(), dto.semester(), dto.schoolYear()));
        }

        // Get fresh managed Course entities
        List<Course> courses = courseRepository.findBySpecialityAndLevelAndSemester(
                dto.speciality(), dto.level(), dto.semester());

        if (courses.isEmpty()) {
            throw new InvalidDataException("No courses found for the given speciality, level and semester");
        }

        // Convert to Set of managed entities
        Set<Course> managedCourses = new HashSet<>(courses);

        Survey survey = surveyMapper.toEntity(dto, managedCourses);
        Survey savedSurvey = surveyRepository.save(survey);
        return surveyMapper.toDto(savedSurvey);
    }

    @Override
    @Transactional
    public void deleteSurvey(Long id) {
        Survey survey = surveyRepository.findByIdWithCourses(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey", "id", id));
        survey.getTargetCourses().clear();
        surveyRepository.delete(survey);
    }


    @Override
    public SurveyDto publishSurvey(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey", "id", id));

        if (survey.getTargetCourses().isEmpty()) {
            throw new OperationNotAllowedException("Cannot publish survey with no courses");
        }

        survey.publish();
        return surveyMapper.toDto(surveyRepository.save(survey));
    }

    @Override
    public SurveyDto unpublishSurvey(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey", "id", id));

        survey.unpublish();
        return surveyMapper.toDto(surveyRepository.save(survey));
    }

    @Override
    public SurveyDto updateSurveyDates(Long surveyId, UpdateSurveyDatesDto dto) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey", "id", surveyId));

        if (survey.isPublished()) {
            if (dto.newOpenDate() != null) {
                throw new OperationNotAllowedException("Cannot change open date for published survey");
            }

            if (dto.newCloseDate() != null) {
                LocalDateTime now = LocalDateTime.now();
                if (dto.newCloseDate().isBefore(now)) {
                    throw new InvalidDataException("Close date must be in the future");
                }
                survey.setCloseDate(dto.newCloseDate());
            }
        } else {
            survey.setOpenDate(dto.newOpenDate());
            survey.setCloseDate(dto.newCloseDate());
        }

        return surveyMapper.toDto(surveyRepository.save(survey));
    }


    @Override
    public SurveySubmissionDetailsDto submitSurveyResponse(Long surveyId, CreateSurveySubmissionDto createSurveySubmissionDto, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        Survey survey = surveyRepository.findByIdWithCourses(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey", "id", surveyId));


        if (!survey.isActive()) {
            throw new OperationNotAllowedException("Survey is not currently active");
        }

        if (surveyResponseRepository.existsByStudentAndSurvey(student, survey)) {
            throw new OperationNotAllowedException("Student already submitted this survey");
        }
        // Validate all courses belong to the survey
        Set<Long> surveyCourseIds = survey.getTargetCourses().stream()
                .map(Course::getId)
                .collect(Collectors.toSet());

        for (CreateSurveySubmissionDto.CourseResponseDto courseResponse : createSurveySubmissionDto.courseResponses()) {
            if (!surveyCourseIds.contains(courseResponse.courseId())) {
                throw new InvalidDataException("Course does not belong to this survey");
            }
        }

        // Get all questions in order
        List<QuestionTemplate> questions = questionTemplateRepository.findAllOrdered();


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


            for (int i = 0; i < questions.size(); i++) {
                Answer answer = Answer.builder()
                        .submission(response)
                        .question(questions.get(i))
                        .course(courseRepository.findById(courseResponse.courseId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        "Course", "id", courseResponse.courseId()
                                )))
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
                .orElseThrow(() -> new ResourceNotFoundException("SurveySubmission", "surveyId and studentId", surveyId + "-" + studentId));
        return toResponseDetailsDto(response);
    }

    @Override
    public List<SurveyDto> getActiveSurveysForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

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
                .toList();


        return surveys.stream()
                .map(surveyMapper::toDto)
                .toList();
    }

    @Override
    public Long getEligibleStudentsCount(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey", "id", surveyId));

        // Calculate the expected school years based on survey's school year and level
        String[] parts = survey.getSchoolYear().split("/");
        int startYear = Integer.parseInt(parts[0]);

        List<String> schoolYears = new ArrayList<>();
        for (int i = 0; i < survey.getLevel(); i++) {
            int currentStart = startYear - i;
            schoolYears.add(currentStart + "/" + (currentStart + 1));
        }

        return studentRepository.countBySpecialityAndCurrentLevelAndEntrySchoolYearIn(
                survey.getSpeciality(),
                survey.getLevel(),
                schoolYears
        );
    }

    @Override
    public Page<SurveyDto> getAllSurveys(SurveyFilterParams filterParams, Pageable pageable) {
        Page<Survey> surveys = surveyRepository.findAllWithFilters(
                filterParams.speciality(),
                filterParams.schoolYear(),
                filterParams.level(),
                filterParams.semester(),
                filterParams.isPublished(),
                filterParams.isActive(),
                pageable
        );

        return surveys.map(surveyMapper::toDto);
    }

    // In SurveyServiceImpl.java
    @Override
    public SurveyStatsDto getSurveyStats(Long surveyId) {
        Survey survey = surveyRepository.findByIdWithCourses(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey", "id", surveyId));

        // Get eligible students count
        long eligibleStudentsCount = getEligibleStudentsCount(surveyId);

        // Calculate participation rate
        long totalSubmissions = surveyResponseRepository.countBySurveyId(surveyId);
        double participationRate = eligibleStudentsCount > 0 ?
                (double) totalSubmissions / eligibleStudentsCount * 100 : 0;

        // Get all questions in order
        List<QuestionTemplate> questions = questionTemplateRepository.findAllOrdered();

        // Calculate course statistics
        List<SurveyStatsDto.CourseStatsDto> courseStats = survey.getTargetCourses().stream()
                .map(course -> {
                    // Calculate average rating per course
                    double courseAverage = calculateCourseAverage(surveyId, course.getId());

                    // Calculate average per question for this course
                    Map<Integer, Double> questionAverages = IntStream.range(0, questions.size())
                            .boxed()
                            .collect(Collectors.toMap(
                                    i -> i + 1, // question order (1-based)
                                    i -> calculateQuestionAverage(surveyId, course.getId(), questions.get(i).getId())
                            ));

                    return new SurveyStatsDto.CourseStatsDto(
                            course.getId(),
                            course.getName(),
                            course.getCode(),
                            courseAverage,
                            questionAverages
                    );
                })
                .toList();

        // Get question distribution (how many times each rating was selected across all questions)
        Map<Integer, Integer> questionDistribution = calculateQuestionDistribution(surveyId);

        // Collect all open feedback
        List<String> openFeedback = survey.getSubmissions().stream()
                .filter(sub -> sub.getOpenFeedback() != null && !sub.getOpenFeedback().isEmpty())
                .map(SurveySubmission::getOpenFeedback)
                .toList();

        return new SurveyStatsDto(
                (int) totalSubmissions,
                (int) eligibleStudentsCount,
                participationRate,
                courseStats,
                questionDistribution,
                openFeedback
        );
    }

    private double calculateCourseAverage(Long surveyId, Long courseId) {
        List<Answer> answers = surveyResponseRepository.findAnswersBySurveyAndCourse(surveyId, courseId);
        if (answers.isEmpty()) return 0;
        return answers.stream()
                .mapToInt(Answer::getRating)
                .average()
                .orElse(0);
    }

    private double calculateQuestionAverage(Long surveyId, Long courseId, Long questionId) {
        List<Answer> answers = surveyResponseRepository.findAnswersBySurveyAndCourseAndQuestion(surveyId, courseId, questionId);
        if (answers.isEmpty()) return 0;
        return answers.stream()
                .mapToInt(Answer::getRating)
                .average()
                .orElse(0);
    }

    private Map<Integer, Integer> calculateQuestionDistribution(Long surveyId) {
        // Initialize with all possible ratings (1-4)
        Map<Integer, Integer> distribution = IntStream.rangeClosed(1, 4)
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> 0));

        // Get all answers for this survey
        List<Answer> answers = surveyResponseRepository.findAllBySurveyId(surveyId);

        // Count each rating occurrence
        answers.forEach(answer -> {
            int rating = answer.getRating();
            distribution.put(rating, distribution.get(rating) + 1);
        });

        return distribution;
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
