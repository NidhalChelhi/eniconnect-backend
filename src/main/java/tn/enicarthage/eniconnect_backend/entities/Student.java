package tn.enicarthage.eniconnect_backend.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import tn.enicarthage.eniconnect_backend.enums.Filiere;
import tn.enicarthage.eniconnect_backend.enums.Niveau;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "cin", nullable = false, unique = true)
    private String cin;

    @Column(name = "filiere", nullable = false)
    @Enumerated(EnumType.STRING)
    private Filiere filiere;

    @Column(name = "niveau", nullable = false)
    @Enumerated(EnumType.STRING)
    private Niveau niveau;

    @Column(name = "classe", nullable = false)
    private String classe;

    @Column(name = "school_year", nullable = false) // e.g. 2023-2024
    private String  schoolYear;

    // optional fields and which can be edited by the student
    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "personal_email", unique = true)
    private String personalEmail;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "bio", unique = true)
    private String bio;

    @Column(name = "is_verified", columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false)
    private boolean isVerified;

    @Column(name = "account_creation_token")
    private String accountCreationToken;
}
