package seniorproject.bankifycore.service;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import seniorproject.bankifycore.domain.Customer;
import seniorproject.bankifycore.domain.enums.CustomerStatus;
import seniorproject.bankifycore.domain.enums.CustomerType;
import seniorproject.bankifycore.dto.request.CreateCustomerRequest;
import seniorproject.bankifycore.dto.request.UpdateCustomerRequest;
import seniorproject.bankifycore.dto.response.CustomerResponse;
import seniorproject.bankifycore.repository.CustomerRepository;
import seniorproject.bankifycore.utils.EnumMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepo;

    // Get all the list of customers
    public List<CustomerResponse> getCustomers() {
        List<Customer> customers = customerRepo.findAll();

        return customers.stream()
                .map(this::toResponse)
                .toList();
    }

    // helper that help Entity -> DTO
    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getType(),
                customer.getStatus());
    }

    // Create a customer
    @Transactional
    public CustomerResponse create(CreateCustomerRequest req) {

        // check if the email is unique
        if (customerRepo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Customer customer = Customer.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .email(req.email())
                .phoneNumber(req.phoneNumber())
                .type(EnumMapper.toEnum(CustomerType.class, req.type()))
                .status(CustomerStatus.ACTIVE)
                .build();

        customerRepo.save(customer);
        return toResponse(customer);
    }

    public CustomerResponse getCustomerById(UUID id) {
        return toResponse(byId(id));
    }

    // helper method to find by id
    private Customer byId(UUID id) {
        return customerRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Customer not found" + id));
    }

    public CustomerResponse updateCustomer(UUID id, UpdateCustomerRequest req) {
        Customer customer = byId(id);

        if (req.firstName() != null) {
            customer.setFirstName(req.firstName());
        }

        if (req.lastName() != null) {
            customer.setLastName(req.lastName());
        }

        if (req.phone() != null) {
            customer.setPhoneNumber(req.phone());
        }

        if (req.status() != null) {
            customer.setStatus(EnumMapper.toEnum(CustomerStatus.class, req.status()));
        }

        Customer saved = customerRepo.save(customer);

        return toResponse(saved);
    }

}
