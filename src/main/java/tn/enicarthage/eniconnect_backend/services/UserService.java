package tn.enicarthage.eniconnect_backend.services;

import tn.enicarthage.eniconnect_backend.dtos.base.UserDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.ChangePasswordDTO;
import tn.enicarthage.eniconnect_backend.dtos.request_response.StudentRegistrationDTO;

public interface UserService {
    UserDTO getUserById(Long id);
    UserDTO registerStudent(StudentRegistrationDTO registrationDTO);
    void changePassword(Long userId, ChangePasswordDTO changePasswordDTO);
}