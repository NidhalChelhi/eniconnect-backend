package tn.enicarthage.eniconnect_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyResponseDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyResponseSummaryDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveySubmissionDTO;
import tn.enicarthage.eniconnect_backend.security.UserPrincipal;
import tn.enicarthage.eniconnect_backend.services.SurveyResponseService;

import java.util.List;

@RestController
@RequestMapping("/survey-responses")
@RequiredArgsConstructor
public class SurveyResponseController {
    private final SurveyResponseService surveyResponseService;

    @PostMapping
    public ResponseEntity<Void> submitSurveyResponse(
            @RequestBody SurveySubmissionDTO submission,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        surveyResponseService.submitSurveyResponse(submission, userPrincipal.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/survey/{surveyId}/student")
    public ResponseEntity<SurveyResponseDTO> getStudentResponse(
            @PathVariable Long surveyId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(surveyResponseService.getStudentResponse(surveyId, userPrincipal.getId()));
    }

    @GetMapping("/survey/{surveyId}/responses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SurveyResponseSummaryDTO>> getSurveyResponses(@PathVariable Long surveyId) {
        return ResponseEntity.ok(surveyResponseService.getSurveyResponses(surveyId));
    }

    @GetMapping("/survey/{surveyId}/has-completed")
    public ResponseEntity<Boolean> hasStudentCompletedSurvey(
            @PathVariable Long surveyId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(surveyResponseService.hasStudentCompletedSurvey(surveyId, userPrincipal.getId()));
    }
}