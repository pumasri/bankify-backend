package seniorproject.bankifycore.dto.atm;

import java.math.BigDecimal;

public record AtmDepositRequest(
        BigDecimal amount, String note
) {
}
