package seniorproject.bankifycore.web.partner;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import seniorproject.bankifycore.dto.RotateKeyRequest;
import seniorproject.bankifycore.dto.RotateKeyResponse;
import seniorproject.bankifycore.dto.RotationRequestItem;
import seniorproject.bankifycore.dto.partner.*;
import seniorproject.bankifycore.service.partner.PartnerAuthService;
import seniorproject.bankifycore.service.partner.PartnerPortalService;
import seniorproject.bankifycore.service.partner.PartnerSignupService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/partner")
public class PartnerPortalController {

    private final PartnerSignupService signupService;
    private final PartnerAuthService partnerAuthService;
    private final PartnerPortalService partnerPortalService;


    @PostMapping("/auth/signup")
    public PartnerSignupResponse signup(@RequestBody PartnerSignupRequest req) {
        return signupService.signup(req);
    }

    // ✅ public
    @PostMapping("/auth/login")
    public PartnerLoginResponse login(@RequestBody PartnerLoginRequest req) {
        return partnerAuthService.login(req);
    }

    // ✅ requires PARTNER_PORTAL JWT
    @GetMapping("/portal/me")
    @PreAuthorize("hasRole('PARTNER')")
    public PartnerPortalMeResponse me() {
        return partnerPortalService.me();
    }


    @PostMapping("/portal/keys/rotate-request")
    @PreAuthorize("hasRole('PARTNER')")
    public RotateKeyResponse requestRotation(@RequestBody RotateKeyRequest req) {
        return partnerPortalService.requestRotation(req);
    }

    @GetMapping("/portal/keys/rotation-requests")
    @PreAuthorize("hasRole('PARTNER')")
    public List<RotationRequestItem> rotationRequests() {
        return partnerPortalService.myRotationRequests();
    }

}
