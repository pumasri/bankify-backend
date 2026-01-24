package seniorproject.bankifycore.dto.partner;

import java.math.BigDecimal;

public record PartnerDepositRequest(
        BigDecimal amount,
        String note
) {
}
