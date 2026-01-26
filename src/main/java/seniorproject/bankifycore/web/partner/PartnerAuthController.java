package seniorproject.bankifycore.web.partner;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import seniorproject.bankifycore.dto.partner.PartnerSignupRequest;
import seniorproject.bankifycore.dto.partner.PartnerSignupResponse;
import seniorproject.bankifycore.service.partner.PartnerSignupService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/partner/auth")
public class PartnerAuthController {

    private final PartnerSignupService signupService;

    @PostMapping("/signup")
    public PartnerSignupResponse signup(@RequestBody PartnerSignupRequest req) {
        return signupService.signup(req);
    }
}
