package tn.enicarthage.eniconnect_backend.dtos.request.student;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UpdateStudentProfileDto(
        @Size(max = 100) String personalEmail,
        @Pattern(regexp = "^\\+?[0-9\\s-]{8,20}$") String phoneNumber,
        @Size(max = 500) String bio,
        @Size(max = 100) @URL String linkedinUrl,
        @Size(max = 100) @URL String githubUrl,
        @Size(max = 255) @URL String profilePictureUrl
) {}