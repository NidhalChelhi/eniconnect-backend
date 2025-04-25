package tn.enicarthage.eniconnect_backend.dtos.survey;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SurveyTemplateDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean isSystemDefault;
}