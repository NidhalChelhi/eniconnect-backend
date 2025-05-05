package tn.enicarthage.eniconnect_backend.dtos.request.complaint;

import jakarta.validation.constraints.NotBlank;
import tn.enicarthage.eniconnect_backend.enums.ComplaintType;

public record CreateComplaintDto(
        Long studentId,
        Boolean isAnonymous,
        ComplaintType type,
        @NotBlank String message
) {
}
