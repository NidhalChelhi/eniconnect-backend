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
}