package tn.enicarthage.eniconnect_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.dtos.base.*;
import tn.enicarthage.eniconnect_backend.services.AcademicYearService;
import tn.enicarthage.eniconnect_backend.services.SemesterService;
import tn.enicarthage.eniconnect_backend.services.SpecializationService;

import java.util.List;

@RestController
@RequestMapping("/academic")
@RequiredArgsConstructor
public class AcademicController {
    private final AcademicYearService academicYearService;
    private final SemesterService semesterService;
    private final SpecializationService specializationService;

    @GetMapping("/years")
    public ResponseEntity<List<AcademicYearDTO>> getAllAcademicYears() {
        return ResponseEntity.ok(academicYearService.getAllAcademicYears());
    }

    @GetMapping("/semesters")
    public ResponseEntity<List<SemesterDTO>> getAllSemesters() {
        return ResponseEntity.ok(semesterService.getAllSemesters());
    }

    @GetMapping("/specializations")
    public ResponseEntity<List<SpecializationDTO>> getAllSpecializations() {
        return ResponseEntity.ok(specializationService.getAllSpecializations());
    }
}