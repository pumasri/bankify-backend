package seniorproject.bankifycore.dto.admin;

public record LoginResponse(
         String token,
         String email,
         String role
) {
}
