package seniorproject.bankifycore.dto.atm;

import java.math.BigDecimal;
import java.util.UUID;

public record AtmTransferRequest(
        UUID toAccountId, BigDecimal amount, String note
) {
}
