package tn.enicarthage.eniconnect_backend.controllers;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.enicarthage.eniconnect_backend.dtos.request.course.CreateCourseDto;
import tn.enicarthage.eniconnect_backend.entities.Course;
import tn.enicarthage.eniconnect_backend.services.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Validated
public class CourseController {

    private final CourseService courseService;


    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Course> getCourseByCode(@PathVariable String code) {
        Course course = courseService.getCourseByCode(code);
        return ResponseEntity.ok(course);
    }


    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Course>> getAllCoursesPaged(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<Course> courses = courseService.getAllCourses(pageable);
        return ResponseEntity.ok(courses);
    }


    @PostMapping
    public ResponseEntity<Course> createCourse(@Valid @RequestBody CreateCourseDto createCourseDto) {
        Course createdCourse = courseService.createCourse(createCourseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Course>> createCoursesFromCsv(@RequestParam("file") @NotNull MultipartFile file) {
        List<Course> createdCourses = courseService.createCoursesFromCsv(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourses);
    }

}
