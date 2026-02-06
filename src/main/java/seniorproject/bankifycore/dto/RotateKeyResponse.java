package seniorproject.bankifycore.dto;

import java.util.UUID;

public record RotateKeyResponse(
        UUID requestId, String status //option to add message
) {
}
