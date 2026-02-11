package seniorproject.bankifycore.service.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seniorproject.bankifycore.domain.ClientApp;
import seniorproject.bankifycore.domain.ClientUser;
import seniorproject.bankifycore.domain.enums.ClientStatus;
import seniorproject.bankifycore.domain.enums.ClientUserRole;
import seniorproject.bankifycore.domain.enums.ClientUserStatus;
import seniorproject.bankifycore.dto.partner.PartnerLoginRequest;
import seniorproject.bankifycore.dto.partner.PartnerLoginResponse;
import seniorproject.bankifycore.dto.partner.PartnerSignupRequest;
import seniorproject.bankifycore.dto.partner.PartnerSignupResponse;
import seniorproject.bankifycore.repository.ClientAppRepository;
import seniorproject.bankifycore.repository.ClientUserRepository;
import seniorproject.bankifycore.security.JwtTokenService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartnerAuthService {


    private final ClientUserRepository clientUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final ClientAppRepository clientAppRepo;


    //This method retrieves the UUID of the currently authenticated client from Spring Security’s security context and throws an error if the
    // request is unauthenticated.
    public UUID currentClientId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new IllegalStateException("Partner principal must be clientId UUID");
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



    @Transactional
    public PartnerSignupResponse signup(PartnerSignupRequest req) {

        if (req.appName() == null || req.appName().isBlank()) {
            throw new IllegalArgumentException("appName is required");
        }
        if (req.email() == null || req.email().isBlank()) {
            throw new IllegalArgumentException("email is required");
        }
        if (req.password() == null || req.password().length() < 8) {
            throw new IllegalArgumentException("password must be at least 8 characters");
        }

        // Enforce global-unique email for portal login
        if (clientUserRepo.existsByEmail(req.email().trim().toLowerCase())) {
            throw new IllegalArgumentException("email already registered");
        }

        ClientApp app = ClientApp.builder()
                .name(req.appName().trim())
                .status(ClientStatus.PENDING)
                .apiKeyHash(null)
                .contactEmail(req.email().trim().toLowerCase())
                .callbackUrl(req.callbackUrl())
                .build();

        ClientApp savedApp = clientAppRepo.save(app);

        ClientUser user = ClientUser.builder()
                .email(req.email().trim().toLowerCase())
                .passwordHash(passwordEncoder.encode(req.password()))
                .status(ClientUserStatus.ACTIVE)
                .role(ClientUserRole.OWNER)
                .clientApp(savedApp)
                .build();

        clientUserRepo.save(user);

        return new PartnerSignupResponse(savedApp.getId(), savedApp.getStatus().name());
    }




}
