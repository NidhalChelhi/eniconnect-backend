package tn.enicarthage.eniconnect_backend.dtos;

import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record StudentUpdate(
        String profilePicture,

        LocalDate birthDate,

        String personalEmail,

        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
        String phoneNumber,

        String bio
) {
}
