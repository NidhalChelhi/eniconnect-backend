package tn.enicarthage.eniconnect_backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tn.enicarthage.eniconnect_backend.enums.Filiere;
import tn.enicarthage.eniconnect_backend.enums.Niveau;

public record StudentCreate(
        @NotNull(message = "first name is required")
        @Size(min = 2, max = 50, message = "min is 2 characters and max is 50 characters")
        String firstName,

        @NotNull(message = "last name is required")
        @Size(min = 2, max = 50, message = "min is 2 characters and max is 50 characters")
        String lastName,

        @NotNull(message = "email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = "cin is required")
        @Size(min = 8, max = 8, message = "cin must be 8 characters")
        String cin,

        @NotNull(message = "filiere is required")
        Filiere filiere,

        @NotNull(message = "niveau is required")
        Niveau niveau,

        @NotNull(message = "classe is required")
        String classe,

        @NotNull(message = "school year is required")
        String schoolYear

) {
}
