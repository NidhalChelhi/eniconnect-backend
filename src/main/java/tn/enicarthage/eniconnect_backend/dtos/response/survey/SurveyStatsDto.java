package tn.enicarthage.eniconnect_backend.dtos.response.survey;

import java.util.List;
import java.util.Map;

public record SurveyStatsDto(
        int totalSubmissions,
        int eligibleStudentsCount,
        double participationRate,
        List<CourseStatsDto> courseStats,
        Map<Integer, Integer> questionDistribution,
        List<String> openFeedback
) {
    public record CourseStatsDto(
            Long courseId,
            String courseName,
            String courseCode,
            double averageRating,
            Map<Integer, Double> questionAverages
    ) {}
}