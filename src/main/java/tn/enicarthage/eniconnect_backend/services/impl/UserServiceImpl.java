package tn.enicarthage.eniconnect_backend.services.impl;

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

@Service
@RequiredArgsConstructor
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
                .firstName(registrationDTO.getFirstName())
                .lastName(registrationDTO.getLastName())
                .matricule(generateMatricule(registrationDTO.getSpecializationCode()))
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
        changePasswordDTO.validate(); // Add this line

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPasswordHash())) {
            throw new ValidationException("Old password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    private String generateMatricule(String specializationCode) {
        Long nextStudentNumber = studentRepository.countBySpecializationCode(specializationCode) + 1;
        int currentYear = java.time.Year.now().getValue() % 100; // Last 2 digits of year
        return String.format("%s%02d%04d", specializationCode, currentYear, nextStudentNumber);
    }
}