package seniorproject.bankifycore.dto.account;

import seniorproject.bankifycore.domain.enums.AccountType;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
                @NotBlank UUID customerId,

                @NotBlank AccountType type,

                @NotBlank String currency) {
}
