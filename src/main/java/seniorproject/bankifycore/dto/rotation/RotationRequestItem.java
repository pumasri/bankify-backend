package seniorproject.bankifycore.dto.rotation;

import java.time.Instant;
import java.util.UUID;

public record RotationRequestItem(
                UUID id,
                String status,
                String reason,
                Instant createdAt) {
}
