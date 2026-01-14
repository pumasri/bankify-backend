package seniorproject.bankifycore.dto.transaction;

import seniorproject.bankifycore.domain.enums.TransactionStatus;
import seniorproject.bankifycore.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        TransactionType type,
        TransactionStatus status,
        BigDecimal amount,
        UUID fromAccountId,
        UUID toAccountId,
        String reference,
        String note,
        Instant createdAt
) {
}
