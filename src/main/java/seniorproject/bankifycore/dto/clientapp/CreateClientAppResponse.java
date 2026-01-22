package seniorproject.bankifycore.dto.clientapp;

import java.util.UUID;

public record CreateClientAppResponse(
                UUID id,
                String name,
                String status,
                String apiKey // shown once
) {
}
