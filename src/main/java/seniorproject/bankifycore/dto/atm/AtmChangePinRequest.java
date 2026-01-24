package seniorproject.bankifycore.dto.atm;

public record AtmChangePinRequest(
        String oldPin, String newPin
) {
}
