package seniorproject.bankifycore.dto.partner;

import seniorproject.bankifycore.dto.transaction.TransactionResponse;

import java.util.List;
import java.util.UUID;

public record PartnerTransactionHistoryResponse(
        UUID accountId,
        List<TransactionResponse> transactions
) {
}
