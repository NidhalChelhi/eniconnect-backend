package tn.enicarthage.eniconnect_backend.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.abstracts.CourseService;
import tn.enicarthage.eniconnect_backend.dtos.CourseCreateDTO;
import tn.enicarthage.eniconnect_backend.dtos.CourseUpdateDTO;
import tn.enicarthage.eniconnect_backend.entities.Course;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> findAll() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> findOne(@PathVariable UUID courseId) {
        return ResponseEntity.ok(courseService.findOne(courseId));
    }

    @PostMapping
    public ResponseEntity<Course> createOne(@RequestBody @Valid CourseCreateDTO course) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courseService.createOne(course));
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Course> updateOne(
            @PathVariable UUID courseId,
            @RequestBody @Valid CourseUpdateDTO courseUpdateDTO
    ) {
        return ResponseEntity.ok(courseService.updateOne(courseId, courseUpdateDTO));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteOne(@PathVariable UUID courseId) {
        courseService.deleteOne(courseId);
        return ResponseEntity.noContent().build();
    }
}