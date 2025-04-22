package tn.enicarthage.eniconnect_backend.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.abstracts.QuestionService;
import tn.enicarthage.eniconnect_backend.dtos.QuestionCreateDTO;
import tn.enicarthage.eniconnect_backend.entities.Question;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<List<Question>> findAll() {
        return ResponseEntity.ok(questionService.findAll());
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<Question> findOne(@PathVariable UUID questionId) {
        return ResponseEntity.ok(questionService.findOne(questionId));
    }

    @PostMapping
    public ResponseEntity<Question> createOne(@RequestBody @Valid QuestionCreateDTO questionDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(questionService.createOne(questionDTO));
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteOne(@PathVariable UUID questionId) {
        questionService.deleteOne(questionId);
        return ResponseEntity.noContent().build();
    }
}