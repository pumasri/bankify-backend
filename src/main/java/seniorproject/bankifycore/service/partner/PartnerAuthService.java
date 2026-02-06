package seniorproject.bankifycore.service.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seniorproject.bankifycore.domain.ClientUser;
import seniorproject.bankifycore.domain.enums.ClientStatus;
import seniorproject.bankifycore.domain.enums.ClientUserStatus;
import seniorproject.bankifycore.dto.partner.PartnerLoginRequest;
import seniorproject.bankifycore.dto.partner.PartnerLoginResponse;
import seniorproject.bankifycore.repository.ClientUserRepository;
import seniorproject.bankifycore.security.JwtTokenService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartnerAuthService {


    private final ClientUserRepository clientUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    //This method retrieves the UUID of the currently authenticated client from Spring Security’s security context and throws an error if the
    // request is unauthenticated.
    public UUID currentClientId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new IllegalStateException("Unauthenticated partner request");
        }
        return (UUID) auth.getPrincipal();
    }


    @Transactional(readOnly = true)
    public PartnerLoginResponse login(PartnerLoginRequest req) {
        String email = (req.email() == null) ? null : req.email().trim().toLowerCase();
        if (email == null || email.isBlank() || req.password() == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        ClientUser user = clientUserRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (user.getStatus() != ClientUserStatus.ACTIVE) {
            throw new IllegalStateException("Partner user is disabled");
        }


        // ✅ (app status gate)
        var app = user.getClientApp();
        if (app.getStatus() == ClientStatus.DISABLED || app.getStatus() == ClientStatus.REJECTED) {
            throw new IllegalStateException("Client app is disabled");
        }
        // Recommended: allow PENDING to login (see docs + pending status)
        // If you want to block PENDING too, use:
        // if (app.getStatus() != ClientStatus.ACTIVE) throw new IllegalStateException("Client app is not active");

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtTokenService.generatePartnerPortalToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        return new PartnerLoginResponse(token);
    }
}
