package tn.enicarthage.eniconnect_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.dtos.base.CourseDTO;
import tn.enicarthage.eniconnect_backend.dtos.base.StudentDTO;
import tn.enicarthage.eniconnect_backend.dtos.survey.SurveyDTO;
import tn.enicarthage.eniconnect_backend.security.UserPrincipal;
import tn.enicarthage.eniconnect_backend.services.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/matricule/{matricule}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentDTO> getStudentByMatricule(@PathVariable String matricule) {
        return ResponseEntity.ok(studentService.getStudentByMatricule(matricule));
    }

    @GetMapping("/specialization/{specializationCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StudentDTO>> getStudentsBySpecialization(@PathVariable String specializationCode) {
        return ResponseEntity.ok(studentService.getStudentsBySpecialization(specializationCode));
    }

    @GetMapping("/me")
    public ResponseEntity<StudentDTO> getCurrentStudent(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(studentService.getStudentByUserId(principal.getId()));
    }

    @GetMapping("/{studentId}/surveys")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SurveyDTO>> getSurveysForStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getSurveysForStudent(studentId));
    }



}