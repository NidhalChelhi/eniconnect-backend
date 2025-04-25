package tn.enicarthage.eniconnect_backend.dtos.request_response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class QuestionStatsDTO {
    private String questionText;
    private Map<String, Double> optionPercentages;
    private double averageRating; // 1-4 scale
}