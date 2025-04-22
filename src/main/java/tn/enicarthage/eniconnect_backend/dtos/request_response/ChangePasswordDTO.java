package tn.enicarthage.eniconnect_backend.dtos.request_response;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {
    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 6)
    private String newPassword;

    @NotBlank
    private String confirmPassword;

    // Add this method
    public void validate() {
        if (!newPassword.equals(confirmPassword)) {
            throw new ValidationException("New password and confirmation don't match");
        }
    }
}