package seniorproject.bankifycore.dto.partner;

import java.math.BigDecimal;
import java.util.UUID;

public record PartnerTransferRequest(
        UUID toAccountId,
        BigDecimal amount,
        String note
) {
}
