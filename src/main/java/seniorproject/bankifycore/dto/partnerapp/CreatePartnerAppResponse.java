package seniorproject.bankifycore.dto.partnerapp;

import java.util.UUID;

public record CreatePartnerAppResponse(
        UUID id,
        String name,
        String status,
        String apiKey // shown once
) {
}
