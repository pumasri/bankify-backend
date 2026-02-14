package seniorproject.bankifycore.web.v1.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seniorproject.bankifycore.consants.ApiPaths;
import seniorproject.bankifycore.dto.partner.PartnerLoginRequest;
import seniorproject.bankifycore.dto.partner.PartnerLoginResponse;
import seniorproject.bankifycore.dto.partner.PartnerSignupRequest;
import seniorproject.bankifycore.dto.partner.PartnerSignupResponse;
import seniorproject.bankifycore.service.partner.PartnerPortalAuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.PARTNER)
public class PartnerAuthController {

    private final PartnerPortalAuthService partnerAuthService;

    @PostMapping("/auth/signup")
    public PartnerSignupResponse signup(@RequestBody PartnerSignupRequest req) {
        return partnerAuthService.signup(req);
    }

    // ✅ public
    @PostMapping("/auth/login")
    public PartnerLoginResponse login(@RequestBody PartnerLoginRequest req) {
        return partnerAuthService.login(req);
    }
}
