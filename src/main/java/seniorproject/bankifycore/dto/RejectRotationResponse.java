package seniorproject.bankifycore.dto;

import java.util.UUID;

public record RejectRotationResponse(
        UUID requestId,
        String status
) {
}
