package tn.enicarthage.eniconnect_backend.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.dtos.request.question.QuestionTemplateDto;
import tn.enicarthage.eniconnect_backend.entities.QuestionTemplate;
import tn.enicarthage.eniconnect_backend.services.QuestionService;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping()
    public ResponseEntity<List<QuestionTemplate>> getAllQuestions() {
        List<QuestionTemplate> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @PostMapping()
    public ResponseEntity<QuestionTemplate> createQuestion(@RequestBody QuestionTemplateDto question) {
        QuestionTemplate Savedquestion = questionService.createQuestion(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(Savedquestion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionTemplate> updateQuestion(@RequestBody QuestionTemplateDto question, @PathVariable int id) {
        QuestionTemplate updatedQuestion = questionService.updateeQuestionById(id, question);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedQuestion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.DeleteQuestion(id);
        return ResponseEntity.noContent().build();
    }


}
