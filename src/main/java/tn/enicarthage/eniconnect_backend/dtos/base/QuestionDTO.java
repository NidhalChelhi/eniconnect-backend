package tn.enicarthage.eniconnect_backend.dtos.base;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionDTO {
    @NotBlank
    private String questionText;

    @NotBlank
    private String questionType; // "LIKERT" or "FREE_TEXT"

    private Integer questionOrder;

    private List<String> options; // For LIKERT only
}