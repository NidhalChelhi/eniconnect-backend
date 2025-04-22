package tn.enicarthage.eniconnect_backend.dtos.request_response;

import lombok.Data;

import java.util.List;

@Data
public class CourseStatsDTO {
    private Long courseId;
    private String courseName;
    private List<QuestionStatsDTO> questionStats;
}