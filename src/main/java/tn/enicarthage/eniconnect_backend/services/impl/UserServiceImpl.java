package tn.enicarthage.eniconnect_backend.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.enicarthage.eniconnect_backend.dtos.base.UserDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.ChangePasswordDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.StudentRegistrationDTO;
import tn.enicarthage.eniconnect_backend.entities.*;
import tn.enicarthage.eniconnect_backend.exceptions.*;
import tn.enicarthage.eniconnect_backend.repositories.*;
import tn.enicarthage.eniconnect_backend.services.UserService;
import tn.enicarthage.eniconnect_backend.utils.AcademicUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final SpecializationRepository specializationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserDTO registerStudent(StudentRegistrationDTO registrationDTO) {
        // Validate username pattern
        if (!registrationDTO.getUsername().matches("^[A-Za-z0-9_]{3,20}$")) {
            throw new ValidationException("Username must be 3-20 alphanumeric characters");
        }

        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new AlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }

        Specialization specialization = specializationRepository.findByCode(registrationDTO.getSpecializationCode())
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));

        User user = User.builder()
                .username(registrationDTO.getUsername())
                .passwordHash(passwordEncoder.encode(registrationDTO.getPassword()))
                .email(registrationDTO.getEmail())
                .role("STUDENT")
                .build();

        User savedUser = userRepository.save(user);

        Student student = Student.builder()
                .user(savedUser)
                .firstName(registrationDTO.getFirstName().trim())
                .lastName(registrationDTO.getLastName().trim())
                .matricule(AcademicUtils.generateMatricule(
                        registrationDTO.getSpecializationCode(),
                        studentRepository.countBySpecializationCode(registrationDTO.getSpecializationCode())))
                .specialization(specialization)
                .entryYear(registrationDTO.getEntryYear())
                .build();

        studentRepository.save(student);

        return UserDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    @Override
    public void changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        if (changePasswordDTO.getNewPassword().equals(changePasswordDTO.getOldPassword())) {
            throw new ValidationException("New password must be different from old password");
        }

        if (changePasswordDTO.getNewPassword().length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }
        changePasswordDTO.validate(); // Add this line

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPasswordHash())) {
            throw new ValidationException("Old password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }


}