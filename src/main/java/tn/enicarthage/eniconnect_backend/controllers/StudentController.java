package tn.enicarthage.eniconnect_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.enicarthage.eniconnect_backend.dtos.request.student.CreateStudentDto;
import tn.enicarthage.eniconnect_backend.dtos.request.student.UpdateStudentProfileDto;
import tn.enicarthage.eniconnect_backend.dtos.response.student.StudentDto;
import tn.enicarthage.eniconnect_backend.services.StudentService;

import java.util.List;


@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Validated
public class StudentController {

    private final StudentService studentService;


    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        StudentDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<StudentDto> getStudentByEmail(@PathVariable String email) {
        StudentDto student = studentService.getStudentByEmail(email);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<StudentDto> getStudentByMatricule(@PathVariable String matricule) {
        StudentDto student = studentService.getStudentByMatricule(matricule);
        return ResponseEntity.ok(student);
    }


    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<StudentDto>> getAllStudentsPaged(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<StudentDto> students = studentService.getAllStudents(pageable);
        return ResponseEntity.ok(students);
    }


    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody CreateStudentDto createStudentDto) {
        StudentDto createdStudent = studentService.createStudent(createStudentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(
            @PathVariable Long id,
            @RequestBody UpdateStudentProfileDto updateStudentProfileDto) {
        StudentDto updatedStudent = studentService.updateStudentProfile(id, updateStudentProfileDto);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<List<StudentDto>> createStudentsFromCsv(@RequestParam("file") MultipartFile file) {
        List<StudentDto> createdStudents = studentService.createStudentsFromCsv(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudents);
    }

}
