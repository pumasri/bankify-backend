package seniorproject.bankifycore.dto.partner;

import java.math.BigDecimal;

public record PartnerTransferRequest(
                String accountNumber,
                BigDecimal amount,
                String note) {
}
