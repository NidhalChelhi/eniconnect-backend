package tn.enicarthage.eniconnect_backend.dtos.base;

import lombok.Data;

@Data
public class SemesterCourseDTO {
    private Long id;
    private SemesterDTO semester;
    private SpecializationDTO specialization;
    private CourseDTO course;
    private Integer yearOfStudy;
}