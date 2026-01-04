package seniorproject.bankifycore.dto.account;

import seniorproject.bankifycore.domain.enums.AccountStatus;

public record UpdateAccountRequest(
        AccountStatus status
) {}
