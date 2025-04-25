package tn.enicarthage.eniconnect_backend.dtos.base;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TemplateQuestionDTO {
    private Long id;
    private String questionText;
    private String questionType;
    private List<String> options;
}