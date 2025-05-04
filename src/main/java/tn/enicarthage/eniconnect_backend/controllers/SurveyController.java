package tn.enicarthage.eniconnect_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.*;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveyDto;
import tn.enicarthage.eniconnect_backend.services.SurveyService;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @GetMapping
    public ResponseEntity<List<SurveyDto>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<SurveyDto>> getAllSurveys(Pageable pageable) {
        return ResponseEntity.ok(surveyService.getAllSurveys(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDto> getSurveyById(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyById(id));
    }

    @PostMapping
    public ResponseEntity<SurveyDto> createSurvey(@RequestBody CreateSurveyDto dto) {
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
            @RequestBody UpdateSurveyDatesDto dto) {
        return ResponseEntity.ok(surveyService.updateSurveyDates(id, dto));
    }
}