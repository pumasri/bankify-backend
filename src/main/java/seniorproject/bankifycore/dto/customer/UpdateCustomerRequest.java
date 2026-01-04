package seniorproject.bankifycore.dto.customer;

public record UpdateCustomerRequest (
    String firstName,
    String lastName,
    String phone,
    String status,
    String email
){};