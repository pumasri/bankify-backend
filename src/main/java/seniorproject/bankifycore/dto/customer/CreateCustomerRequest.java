package seniorproject.bankifycore.dto.customer;

import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(

                @NotBlank String firstName,

                @NotBlank String lastName,

                String phoneNumber,

                @NotBlank String email,

                @NotBlank String type

) {
};
