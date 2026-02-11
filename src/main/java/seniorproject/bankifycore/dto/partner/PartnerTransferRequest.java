package seniorproject.bankifycore.dto.partner;

import java.math.BigDecimal;
import java.util.UUID;

public record PartnerTransferRequest(
        String accountNumber,
        BigDecimal amount,
        String note
) {
}
