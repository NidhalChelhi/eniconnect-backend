package tn.enicarthage.eniconnect_backend.dtos.request_response;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegistrationDTO {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String specializationCode;

    @Min(2000)
    @Max(2100)
    private int entryYear;
}