package tn.enicarthage.eniconnect_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tn.enicarthage.eniconnect_backend.dtos.request_response.ChangePasswordDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.StudentRegistrationDTO;
import tn.enicarthage.eniconnect_backend.dtos.base.UserDTO;
import tn.enicarthage.eniconnect_backend.security.UserPrincipal;
import tn.enicarthage.eniconnect_backend.services.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        UserDTO userDTO = userService.getUserById(principal.getId());
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerStudent(@Valid @RequestBody StudentRegistrationDTO registrationDTO) {
        return ResponseEntity.status(201).body(userService.registerStudent(registrationDTO));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(principal.getId(), changePasswordDTO);
        return ResponseEntity.ok().build();
    }
}