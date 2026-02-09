package seniorproject.bankifycore.dto.rotation;

import java.util.UUID;

public record ApproveRotationResponse(
                UUID requestId,
                UUID clientAppId,
                String status,
                String apiKey) {
}
