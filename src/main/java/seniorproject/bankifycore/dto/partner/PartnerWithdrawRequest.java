package seniorproject.bankifycore.dto.partner;

import java.math.BigDecimal;

public record PartnerWithdrawRequest(
        BigDecimal amount,
        String note
) {
}
