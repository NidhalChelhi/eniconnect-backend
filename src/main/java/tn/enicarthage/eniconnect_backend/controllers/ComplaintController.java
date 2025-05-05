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
import tn.enicarthage.eniconnect_backend.dtos.request.complaint.CreateComplaintDto;
import tn.enicarthage.eniconnect_backend.dtos.response.complaint.ComplaintDto;
import tn.enicarthage.eniconnect_backend.services.ComplaintService;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
@Validated
public class ComplaintController {

    private final ComplaintService complaintService;


    @GetMapping("/{id}")
    public ResponseEntity<ComplaintDto> getComplaintById(@PathVariable Long id) {
        ComplaintDto complaint = complaintService.getComplaintById(id);
        return ResponseEntity.ok(complaint);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ComplaintDto>> getComplaintsByStudentId(@PathVariable Long studentId) {
        List<ComplaintDto> complaints = complaintService.getComplaintsByStudentId(studentId);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping
    public ResponseEntity<List<ComplaintDto>> getAllComplaints() {
        List<ComplaintDto> complaints = complaintService.getAllComplaints();
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<ComplaintDto>> getAllComplaintsPaged(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<ComplaintDto> complaints = complaintService.getAllComplaints(pageable);
        return ResponseEntity.ok(complaints);
    }


    @PostMapping
    public ResponseEntity<ComplaintDto> createComplaint(@Valid @RequestBody CreateComplaintDto createComplaintDto) {
        ComplaintDto createdComplaint = complaintService.createComplaint(createComplaintDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComplaint);
    }


    @PutMapping("/{id}/resolve")
    public ResponseEntity<ComplaintDto> resolveComplaint(@PathVariable Long id) {
        ComplaintDto resolvedComplaint = complaintService.resolveComplaint(id);
        return ResponseEntity.ok(resolvedComplaint);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.noContent().build();
    }
}
