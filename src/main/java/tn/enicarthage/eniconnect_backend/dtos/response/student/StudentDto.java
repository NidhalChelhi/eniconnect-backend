package tn.enicarthage.eniconnect_backend.dtos.response.student;

import tn.enicarthage.eniconnect_backend.enums.Speciality;

import java.time.LocalDateTime;

public record StudentDto(
        Long id,
        String matricule,
        String firstName,
        String lastName,
        String email,
        Speciality speciality,
        int currentLevel,
        String groupe,
        String entrySchoolYear,
        String graduationSchoolYear,
        String gender,

        String personalEmail,
        String phoneNumber,
        String bio,
        String linkedinUrl,
        String githubUrl,
        String profilePictureUrl,
        LocalDateTime createdAt
) {
}