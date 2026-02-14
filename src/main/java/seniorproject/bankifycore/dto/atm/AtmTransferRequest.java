package seniorproject.bankifycore.dto.atm;

import java.math.BigDecimal;

public record AtmTransferRequest(
//        UUID toAccountId,//legacy
        String toAccountNumber,
        BigDecimal amount,
        String note
) {
}
