package seniorproject.bankifycore.service.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seniorproject.bankifycore.domain.ClientApp;
import seniorproject.bankifycore.domain.ClientUser;
import seniorproject.bankifycore.domain.enums.ClientStatus;
import seniorproject.bankifycore.domain.enums.ClientUserRole;
import seniorproject.bankifycore.domain.enums.ClientUserStatus;
import seniorproject.bankifycore.dto.partner.PartnerSignupRequest;
import seniorproject.bankifycore.dto.partner.PartnerSignupResponse;
import seniorproject.bankifycore.repository.ClientAppRepository;
import seniorproject.bankifycore.repository.ClientUserRepository;

@Service
@RequiredArgsConstructor
public class PartnerSignupService {

    private final ClientAppRepository clientAppRepo;
    private final ClientUserRepository clientUserRepo;
    private final PasswordEncoder passwordEncoder;



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
