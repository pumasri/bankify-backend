package seniorproject.bankifycore.service.partner;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PartnerAuthService {

    //This method retrieves the UUID of the currently authenticated client from Spring Security’s security context and throws an error if the
    // request is unauthenticated.
    public UUID currentClientId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new IllegalStateException("Unauthenticated partner request");
        }
        return (UUID) auth.getPrincipal();
    }
}
