package seniorproject.bankifycore.dto.partner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PartnerBalanceResponse(
        UUID accountId,
        BigDecimal balance,
        String currency,
        Instant updatedAt
) {
}
