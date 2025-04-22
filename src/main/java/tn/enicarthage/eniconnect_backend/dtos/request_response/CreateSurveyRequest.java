package tn.enicarthage.eniconnect_backend.dtos.request_response;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateSurveyRequest {
    @NotNull
    private Long templateId;

    @NotNull
    private Long semesterId;

    @NotNull
    private Long specializationId;

    @Min(1) @Max(3)
    private Integer yearOfStudy;

    @NotBlank
    private String title;

    private String description;

    @Future
    private LocalDateTime openDate;

    @Future
    private LocalDateTime closeDate;
}