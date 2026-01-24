package seniorproject.bankifycore.dto.atm;

import java.math.BigDecimal;

public record AtmWithdrawRequest(
        BigDecimal amount, String note
) {
}
