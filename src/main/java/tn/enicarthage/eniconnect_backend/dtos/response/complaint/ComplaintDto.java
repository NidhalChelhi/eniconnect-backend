package tn.enicarthage.eniconnect_backend.dtos.response.complaint;

import tn.enicarthage.eniconnect_backend.dtos.response.student.StudentDto;

import java.time.LocalDateTime;

public record ComplaintDto(
        Long id,
        StudentDto student,
        Boolean isAnonymous,
        String type,
        String message,
        String status,
        String createdAt,
        LocalDateTime resolvedAt
) {
}
