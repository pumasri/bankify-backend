package seniorproject.bankifycore.dto.rotation;

import java.util.UUID;

public record RejectRotationResponse(
                UUID requestId,
                String status) {
}
