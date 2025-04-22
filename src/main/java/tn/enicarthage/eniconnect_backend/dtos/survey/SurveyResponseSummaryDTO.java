package tn.enicarthage.eniconnect_backend.dtos.survey;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SurveyResponseSummaryDTO {
    private Long responseId;
    private String studentName;
    private String studentMatricule;
    private LocalDateTime submissionDate;
}