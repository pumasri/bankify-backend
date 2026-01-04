package seniorproject.bankifycore.dto.customer;

import seniorproject.bankifycore.domain.enums.CustomerStatus;
import seniorproject.bankifycore.domain.enums.CustomerType;

import java.util.UUID;


public record CustomerResponse (
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        CustomerType type,
        CustomerStatus status
){};
