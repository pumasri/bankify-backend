package seniorproject.bankifycore.dto.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawRequest(
        UUID accountId,
        BigDecimal amount,
        String note
) {
}
