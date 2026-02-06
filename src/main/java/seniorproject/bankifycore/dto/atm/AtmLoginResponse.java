package seniorproject.bankifycore.dto.atm;

public record AtmLoginResponse(
    String token,
    boolean pinChangeRequired
) {
}
