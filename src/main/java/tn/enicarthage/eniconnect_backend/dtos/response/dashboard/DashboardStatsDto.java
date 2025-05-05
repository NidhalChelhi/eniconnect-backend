package tn.enicarthage.eniconnect_backend.dtos.response.dashboard;

import java.util.Map;

public record DashboardStatsDto(
        int totalStudents,
        int totalSurveys,
        int totalComplaints,
        int totalCourses,
        double surveyParticipationRate,
        Map<String, Integer> complaintsByType,
        Map<String, Integer> surveysByStatus,
        Map<String, Integer> submissionsByMonth
) {
}