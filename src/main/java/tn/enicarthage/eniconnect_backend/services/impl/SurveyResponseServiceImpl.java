package tn.enicarthage.eniconnect_backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.enicarthage.eniconnect_backend.dtos.request_response.SurveyStatsDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.*;
import tn.enicarthage.eniconnect_backend.entities.*;
import tn.enicarthage.eniconnect_backend.exceptions.*;
import tn.enicarthage.eniconnect_backend.repositories.*;
import tn.enicarthage.eniconnect_backend.services.SurveyResponseService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyResponseServiceImpl implements SurveyResponseService {

    private final SurveyResponseRepository responseRepository;
    private final SurveyRepository surveyRepository;
    private final StudentRepository studentRepository;
    private final SpecializationRepository specializationRepository;

    @Override
    @Transactional
    public void submitSurveyResponse(SurveySubmissionDTO submission, Long studentId) {
        Survey survey = surveyRepository.findById(submission.getSurveyId())
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // Basic validation
        if (!survey.getIsActive()) {
            throw new SurveyClosedException("This survey is not currently active");
        }

        if (survey.getCloseDate().isBefore(LocalDateTime.now())) {
            throw new SurveyClosedException("This survey has already closed");
        }

        if (responseRepository.existsBySurveyIdAndStudentId(survey.getId(), student.getId())) {
            throw new AlreadyExistsException("You have already completed this survey");
        }

        // Create and save the response
        SurveyResponse response = SurveyResponse.builder()
                .survey(survey)
                .student(student)
                .submittedAt(LocalDateTime.now())
                .isComplete(true)
                .feedback(submission.getFeedback())
                .build();

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

        return SurveyResponseDTO.builder()
                .id(response.getId())
                .submittedAt(response.getSubmittedAt())
                .isComplete(response.getIsComplete())
                .feedback(response.getFeedback())
                .build();
    }

    @Override
    public List<SurveyResponseSummaryDTO> getSurveyResponses(Long surveyId) {
        return responseRepository.findWithStudentDetailsBySurveyId(surveyId)
                .stream()
                .map(response -> SurveyResponseSummaryDTO.builder()
                        .responseId(response.getId())
                        .studentName(response.getStudent().getFirstName() + " " + response.getStudent().getLastName())
                        .studentMatricule(response.getStudent().getMatricule())
                        .submissionDate(response.getSubmittedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public SurveyStatsDTO getSurveyStats(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        Specialization specialization = specializationRepository.findById(survey.getSpecialization().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));

        return SurveyStatsDTO.builder()
                .surveyId(surveyId)
                .surveyTitle(survey.getTitle())
                .totalResponses(responseRepository.countBySurveyId(surveyId))
                .totalStudents(studentRepository.countBySpecialization(specialization))
                .build();
    }
}