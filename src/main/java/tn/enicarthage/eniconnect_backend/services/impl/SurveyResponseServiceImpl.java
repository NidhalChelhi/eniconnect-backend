package tn.enicarthage.eniconnect_backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.survey.*;
import tn.enicarthage.eniconnect_backend.entities.*;
import tn.enicarthage.eniconnect_backend.exceptions.*;
import tn.enicarthage.eniconnect_backend.repositories.*;
import tn.enicarthage.eniconnect_backend.services.SurveyResponseService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyResponseServiceImpl implements SurveyResponseService {
    private final SurveyResponseRepository responseRepository;
    private final SurveyRepository surveyRepository;
    private final StudentRepository studentRepository;
    private final SemesterCourseRepository semesterCourseRepository;
    private final SurveyQuestionRepository questionRepository;

    @Override
    @Transactional
    public void submitSurveyResponse(SurveySubmissionDTO submission, Long studentId) {
        Optional<Survey> surveyOptional = surveyRepository.findById(submission.getSurveyId());
        Survey survey = surveyOptional.orElseThrow(() ->
                new ResourceNotFoundException("Survey not found"));

        if (!survey.getIsActive()) {
            throw new SurveyClosedException("This survey is no longer active");
        }

        Optional<Student> studentOptional = studentRepository.findById(studentId);
        Student student = studentOptional.orElseThrow(() ->
                new ResourceNotFoundException("Student not found"));

        if (responseRepository.existsBySurveyIdAndStudentId(survey.getId(), student.getId())) {
            throw new AlreadyExistsException("You have already completed this survey");
        }

        SurveyResponse response = new SurveyResponse();
        response.setSurvey(survey);
        response.setStudent(student);
        response.setSubmittedAt(LocalDateTime.now());
        response.setIsComplete(true);
        response.setFeedback(submission.getFeedback());

        responseRepository.save(response);

        submission.getResponses().forEach(courseResponse -> {
            Optional<SemesterCourse> courseOptional = semesterCourseRepository.findById(courseResponse.getSemesterCourseId());
            SemesterCourse semesterCourse = courseOptional.orElseThrow(() ->
                    new ResourceNotFoundException("Course not found"));

            CourseResponse cr = new CourseResponse();
            cr.setSurveyResponse(response);
            cr.setSemesterCourse(semesterCourse);
            cr.setOrder(courseResponse.getOrder());

            response.getCourseResponses().add(cr);

            courseResponse.getAnswers().forEach(answer -> {
                Optional<SurveyQuestion> questionOptional = questionRepository.findById(answer.getQuestionId());
                SurveyQuestion question = questionOptional.orElseThrow(() ->
                        new ResourceNotFoundException("Question not found"));

                QuestionResponse qr = new QuestionResponse();
                qr.setCourseResponse(cr);
                qr.setSurveyQuestion(question);
                validateResponse(question, answer.getResponse());
                qr.setResponseValue(answer.getResponse());
                qr.setResponseTime(LocalDateTime.now());

                cr.getQuestionResponses().add(qr);
            });
        });

        responseRepository.save(response);
    }


    @Override
    public boolean hasStudentCompletedSurvey(Long surveyId, Long studentId) {
        return responseRepository.existsBySurveyIdAndStudentId(surveyId, studentId);
    }

    @Override
    public SurveyResponseDTO getStudentResponse(Long surveyId, Long studentId) {
        SurveyResponse response = responseRepository.findBySurveyIdAndStudentId(surveyId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Response not found"));

        return convertToDTO(response);
    }

    @Override
    public List<SurveyResponseSummaryDTO> getSurveyResponses(Long surveyId) {
        return responseRepository.findBySurveyId(surveyId).stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    private SurveyResponseDTO convertToDTO(SurveyResponse response) {
        return SurveyResponseDTO.builder()
                .id(response.getId())
                .submittedAt(response.getSubmittedAt())
                .feedback(response.getFeedback())
                .courseResponses(response.getCourseResponses().stream()
                        .map(this::convertCourseResponseToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private CourseResponseDTO convertCourseResponseToDTO(CourseResponse courseResponse) {
        return CourseResponseDTO.builder()
                .id(courseResponse.getId())
                .courseName(courseResponse.getSemesterCourse().getCourse().getName())
                .responses(courseResponse.getQuestionResponses().stream()
                        .map(this::convertQuestionResponseToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private QuestionResponseDTO convertQuestionResponseToDTO(QuestionResponse questionResponse) {
        return QuestionResponseDTO.builder()
                .id(questionResponse.getId())
                .questionId(questionResponse.getSurveyQuestion().getId())
                .questionText(questionResponse.getSurveyQuestion().getQuestionText())
                .responseValue(questionResponse.getResponseValue())
                .build();
    }

    private SurveyResponseSummaryDTO convertToSummaryDTO(SurveyResponse response) {
        return SurveyResponseSummaryDTO.builder()
                .responseId(response.getId())
                .studentName(response.getStudent().getFirstName() + " " + response.getStudent().getLastName())
                .studentMatricule(response.getStudent().getMatricule())
                .submissionDate(response.getSubmittedAt())
                .build();
    }

    private void validateResponse(SurveyQuestion question, String responseValue) {
        if ("LIKERT".equals(question.getQuestionType())) {
            List<String> validOptions = Arrays.asList(question.getOptions().split(","));
            if (!validOptions.contains(responseValue)) {
                throw new ValidationException("Invalid response for Likert scale question");
            }
        }
    }

}