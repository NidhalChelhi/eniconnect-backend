package tn.enicarthage.eniconnect_backend.services;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.enicarthage.eniconnect_backend.config.JwtHelper;
import tn.enicarthage.eniconnect_backend.dtos.LoginRequest;
import tn.enicarthage.eniconnect_backend.dtos.SignupRequest;
import tn.enicarthage.eniconnect_backend.entities.Student;
import tn.enicarthage.eniconnect_backend.entities.UserAccount;
import tn.enicarthage.eniconnect_backend.repositories.StudentRepository;
import tn.enicarthage.eniconnect_backend.repositories.UserAccountRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserAccountRepository userAccountRepo;
    private final StudentRepository studentRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;


    public void signup(SignupRequest signupRequest, String token) {
        Student student = studentRepo.findOneByAccountCreationToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student with token " + token + " not found"
                ));

        if (student.isVerified()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Student with token " + token + " already verified"
            );
        }

        UserAccount account = new UserAccount();
        account.setUsername(signupRequest.username());
        account.setPassword(passwordEncoder.encode(signupRequest.password()));
        account.setStudent(student);
        userAccountRepo.save(account);

        student.setVerified(true);
        student.setAccountCreationToken(null);
        studentRepo.save(student);
    }


    public String login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        UserAccount user = userAccountRepo.findOneByUsername(loginRequest.username())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User with username " + loginRequest.username() + " not found"
                        ));

        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("userId", user.getId());
        return jwtHelper.generateToken(customClaims, user);
    }
}
