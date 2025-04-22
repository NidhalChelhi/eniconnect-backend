package tn.enicarthage.eniconnect_backend.dtos.request_response;

import lombok.Data;

@Data
public class ResponseCountDTO {
    private String option;
    private int count;
    private double percentage;
}