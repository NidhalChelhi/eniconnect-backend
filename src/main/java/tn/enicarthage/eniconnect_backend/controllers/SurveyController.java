package tn.enicarthage.eniconnect_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.dtos.base.CourseDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.CreateSurveyRequest;
import tn.enicarthage.eniconnect_backend.dtos.request_response.SurveyStatsDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyQuestionDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyWithQuestionsDTO;
import tn.enicarthage.eniconnect_backend.security.UserPrincipal;
import tn.enicarthage.eniconnect_backend.services.SurveyService;

import java.util.List;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SurveyDTO> createSurvey(@RequestBody CreateSurveyRequest request) {
        return ResponseEntity.ok(surveyService.createSurvey(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDTO> getSurveyById(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<SurveyDTO>> getActiveSurveys() {
        return ResponseEntity.ok(surveyService.getActiveSurveys());
    }

    @GetMapping("/specialization/{specializationCode}/year/{yearOfStudy}")
    public ResponseEntity<List<SurveyDTO>> getSurveysBySpecializationAndYear(
            @PathVariable String specializationCode,
            @PathVariable int yearOfStudy) {
        return ResponseEntity.ok(surveyService.getSurveysBySpecializationAndYear(specializationCode, yearOfStudy));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activateSurvey(@PathVariable Long id) {
        surveyService.activateSurvey(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> closeSurvey(@PathVariable Long id) {
        surveyService.closeSurvey(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SurveyStatsDTO> getSurveyStats(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyStats(id));
    }

    @GetMapping("/{id}/with-questions")
    public ResponseEntity<SurveyWithQuestionsDTO> getSurveyWithQuestions(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyWithQuestions(id));
    }

    @GetMapping("/templates/{templateId}/questions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SurveyQuestionDTO>> getSurveyTemplateQuestions(@PathVariable Long templateId) {
        return ResponseEntity.ok(surveyService.getSurveyTemplateQuestions(templateId));
    }


    @GetMapping("/for-student")
    public ResponseEntity<List<SurveyDTO>> getSurveysForCurrentStudent(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(surveyService.getSurveysForStudent(principal.getId()));
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseDTO>> getCoursesForSurvey(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getCoursesForSurvey(id));
    }
}