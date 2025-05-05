package tn.enicarthage.eniconnect_backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.response.dashboard.DashboardStatsDto;
import tn.enicarthage.eniconnect_backend.enums.ComplaintType;
import tn.enicarthage.eniconnect_backend.repositories.*;
import tn.enicarthage.eniconnect_backend.services.DashboardService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final StudentRepository studentRepository;
    private final SurveyRepository surveyRepository;
    private final ComplaintRepository complaintRepository;
    private final CourseRepository courseRepository;
    private final SurveyResponseRepository surveyResponseRepository;

    @Override
    public DashboardStatsDto getDashboardStatistics() {
        // Basic counts
        int totalStudents = (int) studentRepository.count();
        int totalSurveys = (int) surveyRepository.count();
        int totalComplaints = (int) complaintRepository.count();
        int totalCourses = (int) courseRepository.count();

        // Survey participation rate (simplified calculation)
        long totalSubmissions = surveyResponseRepository.count();
        double surveyParticipationRate = totalStudents > 0 ?
                (double) totalSubmissions / totalStudents * 100 : 0;

        // Complaints by type
        Map<String, Integer> complaintsByType = Arrays.stream(ComplaintType.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        type -> complaintRepository.countByType(type)
                ));

        // Surveys by status
        Map<String, Integer> surveysByStatus = new HashMap<>();
        surveysByStatus.put("ACTIVE", surveyRepository.countActiveSurveys(LocalDateTime.now()));
        surveysByStatus.put("PUBLISHED", surveyRepository.countByIsPublished(true));
        surveysByStatus.put("DRAFT", surveyRepository.countByIsPublished(false));

        // Submissions by month (last 6 months)
        LocalDateTime now = LocalDateTime.now();
        Map<String, Integer> submissionsByMonth = new LinkedHashMap<>(); // Preserves insertion order

        for (int i = 5; i >= 0; i--) { // Last 6 months including current
            LocalDateTime date = now.minusMonths(i);
            String monthName = date.getMonth().name();
            int count = surveyResponseRepository.countBySubmittedAtMonth(date.getMonthValue());
            submissionsByMonth.put(monthName, count);
        }

        return new DashboardStatsDto(
                totalStudents,
                totalSurveys,
                totalComplaints,
                totalCourses,
                surveyParticipationRate,
                complaintsByType,
                surveysByStatus,
                submissionsByMonth
        );
    }
}