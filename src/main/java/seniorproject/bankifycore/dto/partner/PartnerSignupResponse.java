package seniorproject.bankifycore.dto.partner;

import java.util.UUID;

public record PartnerSignupResponse(
        UUID clientAppId,
        String status
) {
}
