package seniorproject.bankifycore.dto.atm;

import java.math.BigDecimal;
import java.util.UUID;

public record AtmBalanceResponse(
        UUID accountId, BigDecimal balance, String currency
) {
}
