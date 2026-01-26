package seniorproject.bankifycore.dto.admin;

import java.util.UUID;

public record ApproveClientResponse(UUID clientAppId, String status, String apiKey) {
}
