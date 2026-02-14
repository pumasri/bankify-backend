package seniorproject.bankifycore.dto.partner;

import java.util.List;
import java.util.UUID;

public record PartnerPortalMeResponse(
        UUID partnerUserId,
        String email,
        String role,
        UUID partnerAppId,
        String appName,
        String appStatus,
        boolean apiKeyIssued,
        List<PartnerEndpointDoc> endpoints
) {
    public record PartnerEndpointDoc(String method, String path, String description, boolean requiresIdempotencyKey) {}
}
