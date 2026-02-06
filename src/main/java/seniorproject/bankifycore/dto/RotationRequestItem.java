package seniorproject.bankifycore.dto;

import java.time.Instant;
import java.util.UUID;

public record RotationRequestItem(
        UUID id,
        String status,
        String reason,
        Instant createdAt
) {
}
