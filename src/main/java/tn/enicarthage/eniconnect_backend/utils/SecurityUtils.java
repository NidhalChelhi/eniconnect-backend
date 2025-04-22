package tn.enicarthage.eniconnect_backend.utils;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import tn.enicarthage.eniconnect_backend.repositories.UserAccountRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserAccountRepository userAccountRepo;

    public boolean isOwner(UUID incomingEmployeeId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println(userDetails.getUsername());
        System.out.println("HERE " + incomingEmployeeId);

        return userAccountRepo.isOwner(userDetails.getUsername(), incomingEmployeeId);
    }
}
