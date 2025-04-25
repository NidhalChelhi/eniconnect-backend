package tn.enicarthage.eniconnect_backend.dtos.survey;

import lombok.Builder;
import lombok.Data;
import tn.enicarthage.eniconnect_backend.dtos.base.SemesterDTO;
import tn.enicarthage.eniconnect_backend.dtos.base.SpecializationDTO;

import java.time.LocalDateTime;

@Data
@Builder
public class SurveyDTO {
    private Long id;
    private SurveyTemplateDTO template;
    private SemesterDTO semester;
    private SpecializationDTO specialization;
    private Integer yearOfStudy;
    private String title;
    private String description;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private Boolean isActive;



}