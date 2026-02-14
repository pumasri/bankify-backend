package seniorproject.bankifycore.web.v1.partner;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import seniorproject.bankifycore.consants.ApiPaths;
import seniorproject.bankifycore.dto.partner.*;
import seniorproject.bankifycore.dto.rotation.RotateKeyRequest;
import seniorproject.bankifycore.dto.rotation.RotateKeyResponse;
import seniorproject.bankifycore.dto.rotation.RotationRequestItem;
import seniorproject.bankifycore.service.partner.PartnerPortalService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.PARTNER +"/portal")
public class PartnerPortalController {

    private final PartnerPortalService partnerPortalService;


    // ✅ requires PARTNER_PORTAL JWT
    @GetMapping("/me")
    @PreAuthorize("hasRole('PARTNER')")
    public PartnerPortalMeResponse me() {
        return partnerPortalService.me();
    }

    @PostMapping("/keys/rotate-request")
    @PreAuthorize("hasRole('PARTNER')")
    public RotateKeyResponse requestRotation(@RequestBody RotateKeyRequest req) {
        return partnerPortalService.requestRotation(req);
    }

    @GetMapping("/keys/rotation-requests")
    @PreAuthorize("hasRole('PARTNER')")
    public List<RotationRequestItem> rotationRequests() {
        return partnerPortalService.myRotationRequests();
    }

}
