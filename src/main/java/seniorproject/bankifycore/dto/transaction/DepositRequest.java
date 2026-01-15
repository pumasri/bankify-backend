package seniorproject.bankifycore.dto.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositRequest(
        UUID accountId,
        BigDecimal amount,
        String note
) {
}
