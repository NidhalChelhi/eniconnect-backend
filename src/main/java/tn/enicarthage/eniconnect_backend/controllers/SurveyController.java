package tn.enicarthage.eniconnect_backend.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.CreateSurveyDto;
import tn.enicarthage.eniconnect_backend.dtos.request.survey.UpdateSurveyDatesDto;
import tn.enicarthage.eniconnect_backend.dtos.response.survey.SurveyDto;
import tn.enicarthage.eniconnect_backend.services.SurveyService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @GetMapping()
    public ResponseEntity<List<SurveyDto>> getSurveys() {
        List<SurveyDto> surveys = surveyService.getAllSurveys();
        return ResponseEntity.ok(surveys);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<SurveyDto>> getSurveys(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<SurveyDto> surveys = surveyService.getAllSurveys(pageable);
        return ResponseEntity.ok(surveys);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDto> getSurveyById(@PathVariable("id") Long id) {
        SurveyDto surveyDto = surveyService.getSurveyById(id);
        return ResponseEntity.ok(surveyDto);
    }

    @PostMapping("/create")
    public ResponseEntity<SurveyDto> createSurvey(@RequestBody CreateSurveyDto CreateSurveyDto) {
        SurveyDto surveyDto = surveyService.createSurvey(CreateSurveyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(surveyDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SurveyDto> deleteSurvey(@PathVariable("id") Long id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<SurveyDto> togglePublish(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Boolean> payload) {
        SurveyDto surveyDto = payload.get("publish")
                ? surveyService.publishSurvey(id)
                : surveyService.unpublishSurvey(id);
        return ResponseEntity.ok(surveyDto);
    }

    @PatchMapping("/{id}/Change_dates")
    public ResponseEntity<SurveyDto> updateSurveyDates(
            @PathVariable("id") Long surveyId,
            @RequestBody UpdateSurveyDatesDto updateDatesDto) {
        SurveyDto updatedSurvey = surveyService.updateSurveyDates(surveyId, updateDatesDto);
        return ResponseEntity.ok(updatedSurvey);
    }

}
