package tn.enicarthage.eniconnect_backend.dtos.base;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SemesterDTO {
    private Long id;
    private AcademicYearDTO academicYear;
    private Integer number;
    private LocalDate startDate;
    private LocalDate endDate;
}