package tn.enicarthage.eniconnect_backend.dtos.request_response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SurveyStatsDTO {
    private Long surveyId;
    private String surveyTitle;
    private int totalResponses;
    private int totalStudents;
    private List<CourseStatsDTO> courseStats;
    private Map<Long, QuestionStatsDTO> questionStats; // Added
}