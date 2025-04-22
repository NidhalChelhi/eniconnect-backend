package tn.enicarthage.eniconnect_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.abstracts.StudentService;
import tn.enicarthage.eniconnect_backend.dtos.StudentCreate;
import tn.enicarthage.eniconnect_backend.dtos.StudentUpdate;
import tn.enicarthage.eniconnect_backend.entities.Student;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> findAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> findOne(@PathVariable UUID studentId) {
        return ResponseEntity.ok(studentService.findOne(studentId));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteOne(@PathVariable UUID studentId) {
        studentService.deleteOne(studentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{studentId}")
    public ResponseEntity<Student> updateOne(
            @PathVariable UUID studentId,
            @RequestBody @Valid StudentUpdate student) {
        return ResponseEntity.ok(studentService.updateOne(studentId, student));
    }

    @PostMapping
    public ResponseEntity<Student> createOne(
            @RequestBody @Valid StudentCreate student) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(studentService.createOne(student));
    }
}