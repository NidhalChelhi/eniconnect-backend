package tn.enicarthage.eniconnect_backend.dtos.request_response;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}