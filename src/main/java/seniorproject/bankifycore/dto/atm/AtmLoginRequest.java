package seniorproject.bankifycore.dto.atm;

public record AtmLoginRequest(
        String accountNumber,
        String pin
) {
}
