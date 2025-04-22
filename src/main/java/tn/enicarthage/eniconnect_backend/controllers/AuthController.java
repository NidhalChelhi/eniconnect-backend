package tn.enicarthage.eniconnect_backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.dtos.LoginRequest;
import tn.enicarthage.eniconnect_backend.dtos.SignupRequest;
import tn.enicarthage.eniconnect_backend.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest loginRequest
    ) {
        String token = authService.login(loginRequest);

        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @PostMapping("/singup")
    public ResponseEntity<String> signup(
            @RequestBody SignupRequest signupRequest,
            @RequestParam String token
    ) {

        authService.signup(signupRequest, token);
        return new ResponseEntity<>("Signed Up", HttpStatus.CREATED);
    }

}
