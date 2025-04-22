package tn.enicarthage.eniconnect_backend.dtos.request_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private String token;
}