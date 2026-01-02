package seniorproject.bankifycore.web;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import seniorproject.bankifycore.dto.request.CreateCustomerRequest;
import seniorproject.bankifycore.dto.request.UpdateCustomerRequest;
import seniorproject.bankifycore.dto.response.CustomerResponse;
import seniorproject.bankifycore.service.AccountService;
import seniorproject.bankifycore.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final AccountService accountService;
    private final CustomerService customerService;

    @GetMapping
    public List<CustomerResponse> getCustomers() {
        return customerService.getCustomers();
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(
            @Valid @RequestBody CreateCustomerRequest request) {
        CustomerResponse customer = customerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> get(@PathVariable UUID customerId) {
        CustomerResponse customer = customerService.getCustomerById(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> update(@PathVariable UUID customerId,
            @Valid @RequestBody UpdateCustomerRequest request) {

        CustomerResponse updateCustomer = customerService.updateCustomer(customerId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updateCustomer);
    }

}
