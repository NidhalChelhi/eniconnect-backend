package tn.enicarthage.eniconnect_backend.dtos.request_response;

import lombok.Data;

import java.util.List;

@Data
public class QuestionStatsDTO {
    private Long questionId;
    private String questionText;
    private List<ResponseCountDTO> responseCounts;
}