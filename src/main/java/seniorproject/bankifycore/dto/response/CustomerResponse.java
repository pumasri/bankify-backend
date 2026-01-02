package seniorproject.bankifycore.dto.response;

import lombok.Data;
import seniorproject.bankifycore.domain.enums.CustomerStatus;
import seniorproject.bankifycore.domain.enums.CustomerType;

import java.time.OffsetDateTime;
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
