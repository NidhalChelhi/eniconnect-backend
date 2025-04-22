package tn.enicarthage.eniconnect_backend.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.abstracts.SurveyService;
import tn.enicarthage.eniconnect_backend.dtos.SurveyCreateDTO;
import tn.enicarthage.eniconnect_backend.dtos.SurveyResponseDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping
    public ResponseEntity<List<SurveyResponseDTO>> findAll() {
        return ResponseEntity.ok(surveyService.findAll());
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyResponseDTO> findOne(@PathVariable UUID surveyId) {
        return ResponseEntity.ok(surveyService.findOne(surveyId));
    }

    @PostMapping
    public ResponseEntity<SurveyResponseDTO> createOne(@RequestBody @Valid SurveyCreateDTO survey) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(surveyService.createOne(survey));
    }

    @DeleteMapping("/{surveyId}")
    public ResponseEntity<Void> deleteOne(@PathVariable UUID surveyId) {
        surveyService.deleteOne(surveyId);
        return ResponseEntity.noContent().build();
    }
}
