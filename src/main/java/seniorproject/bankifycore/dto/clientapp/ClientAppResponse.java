package seniorproject.bankifycore.dto.clientapp;

import java.time.Instant;
import java.util.UUID;

public record ClientAppResponse(
        UUID id,
        String name,
        String status,
        Instant createdAt
) {
}
