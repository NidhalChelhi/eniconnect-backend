package tn.enicarthage.eniconnect_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveyDto;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveySubmissionDto;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.SurveyFilterParams;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.UpdateSurveyDatesDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveyDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveySubmissionDetailsDto;
import tn.enicarthage.eniconnect_backend.enums.Speciality;
import tn.enicarthage.eniconnect_backend.services.SurveyService;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@Validated
public class SurveyController {
    private final SurveyService surveyService;

    @GetMapping
    public ResponseEntity<List<SurveyDto>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDto> getSurveyById(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyById(id));
    }

    @PostMapping
    public ResponseEntity<SurveyDto> createSurvey(@Valid @RequestBody CreateSurveyDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(surveyService.createSurvey(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<SurveyDto> publishSurvey(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.publishSurvey(id));
    }

    @PatchMapping("/{id}/unpublish")
    public ResponseEntity<SurveyDto> unpublishSurvey(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.unpublishSurvey(id));
    }

    @PatchMapping("/{id}/dates")
    public ResponseEntity<SurveyDto> updateSurveyDates(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSurveyDatesDto dto) {
        return ResponseEntity.ok(surveyService.updateSurveyDates(id, dto));
    }


    @PostMapping("/{id}/respond/{studentId}")
    public ResponseEntity<SurveySubmissionDetailsDto> submitSurveyResponse(
            @PathVariable("id") Long id,
            @Valid @RequestBody CreateSurveySubmissionDto createSurveySubmissionDto,
            @PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(surveyService.submitSurveyResponse(id, createSurveySubmissionDto, studentId));
    }

    @GetMapping("/{surveyId}/responses/{studentId}")
    public ResponseEntity<SurveySubmissionDetailsDto> getStudentResponseForSurvey(
            @PathVariable Long surveyId,
            @PathVariable Long studentId) {
        return ResponseEntity.ok(surveyService.getStudentResponseForSurvey(surveyId, studentId));
    }

    @GetMapping("/active/{studentId}")
    public ResponseEntity<List<SurveyDto>> getActiveSurveysForStudent(
            @PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(surveyService.getActiveSurveysForStudent(studentId));
    }


    @GetMapping("/{surveyId}/eligible-students")
    public ResponseEntity<Long> getEligibleStudentsCount(@PathVariable Long surveyId) {
        return ResponseEntity.ok(surveyService.getEligibleStudentsCount(surveyId));
    }


    @GetMapping("/paged")
    public ResponseEntity<Page<SurveyDto>> getAllSurveysPaged(
            @RequestParam(required = false) Speciality speciality,
            @RequestParam(required = false) String schoolYear,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) Boolean isPublished,
            @RequestParam(required = false) Boolean isActive,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        SurveyFilterParams filterParams = new SurveyFilterParams(
                speciality, schoolYear, level, semester, isPublished, isActive
        );

        return ResponseEntity.ok(surveyService.getAllSurveys(filterParams, pageable));
    }

}