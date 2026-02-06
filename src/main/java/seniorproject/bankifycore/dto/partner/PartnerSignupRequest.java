package seniorproject.bankifycore.dto.partner;

public record PartnerSignupRequest(
        String appName,
        String email,
        String password,
        String callbackUrl
) {
}
