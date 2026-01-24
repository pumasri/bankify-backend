package seniorproject.bankifycore.dto.account;

import jakarta.validation.constraints.NotNull;
import seniorproject.bankifycore.domain.enums.AccountType;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import seniorproject.bankifycore.domain.enums.Currency;

public record CreateAccountRequest(
                @NotNull UUID customerId,

                @NotNull AccountType type,

                @NotNull Currency currency,
                @NotBlank String pin
                ) {
}
