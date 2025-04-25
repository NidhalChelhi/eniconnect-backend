package tn.enicarthage.eniconnect_backend.dtos.base;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String matricule;
    private String specializationCode;
    private String specializationName; // Added
    private int yearOfStudy; // Added
    private String email; // Added from User
}