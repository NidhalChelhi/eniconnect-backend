package tn.enicarthage.eniconnect_backend.dtos.base;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcademicYearDTO {
    private Long id;
    private String name;
    private Integer startYear;
    private Integer endYear;
}