package tn.enicarthage.eniconnect_backend.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.dtos.SurveyQuestionDTO;
import tn.enicarthage.eniconnect_backend.dtos.QuestionInSurveyDTO;
import tn.enicarthage.eniconnect_backend.abstracts.SurveyQuestionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/surveys/{surveyId}/questions")
public class SurveyQuestionController {

    private final SurveyQuestionService surveyQuestionService;

    public SurveyQuestionController(SurveyQuestionService surveyQuestionService) {
        this.surveyQuestionService = surveyQuestionService;
    }

    @GetMapping
    public ResponseEntity<List<SurveyQuestionDTO>> findBySurveyId(
            @PathVariable UUID surveyId) {
        return ResponseEntity.ok(surveyQuestionService.findBySurveyId(surveyId));
    }

    @PostMapping
    public ResponseEntity<List<SurveyQuestionDTO>> addQuestionsToSurvey(
            @PathVariable UUID surveyId,
            @RequestBody @Valid List<QuestionInSurveyDTO> questions) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(surveyQuestionService.addQuestionsToSurvey(surveyId, questions));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeQuestionsFromSurvey(
            @PathVariable UUID surveyId,
            @RequestBody List<UUID> questionIds) {
        surveyQuestionService.removeQuestionsFromSurvey(surveyId, questionIds);
        return ResponseEntity.noContent().build();
    }
}
