package seniorproject.bankifycore.dto.request;

import seniorproject.bankifycore.domain.enums.CustomerStatus;

public record UpdateCustomerRequest (
    String firstName,
    String lastName,
    String phone,
    String status,
    String email
){};