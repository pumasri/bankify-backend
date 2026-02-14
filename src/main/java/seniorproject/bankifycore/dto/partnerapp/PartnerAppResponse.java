package seniorproject.bankifycore.dto.partnerapp;

import java.time.Instant;
import java.util.UUID;

public record PartnerAppResponse(
                UUID id,
                String name,
                String status,
                Instant createdAt) {
}
