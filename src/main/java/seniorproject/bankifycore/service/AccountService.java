package seniorproject.bankifycore.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seniorproject.bankifycore.domain.Account;
import seniorproject.bankifycore.domain.Customer;
import seniorproject.bankifycore.domain.enums.AccountStatus;
import seniorproject.bankifycore.domain.enums.CustomerStatus;
import seniorproject.bankifycore.dto.account.AccountResponse;
import seniorproject.bankifycore.dto.account.CreateAccountRequest;
import seniorproject.bankifycore.dto.account.UpdateAccountRequest;
import seniorproject.bankifycore.repository.AccountRepository;
import seniorproject.bankifycore.repository.CustomerRepository;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepo;
    private final CustomerRepository customerRepo;

    private static final SecureRandom secureRandom = new SecureRandom();

    // ✅ Rule: customer exists
    // ✅ Rule: customer status must be ACTIVE
    // ✅ Rule: generate unique accountNumber
    // ✅ Rule: balance = 0, status = ACTIVE


    @Transactional
    public AccountResponse create(CreateAccountRequest req) {
        Customer customer = customerRepo.findById(req.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if(customer.getStatus() != CustomerStatus.ACTIVE){
            throw new RuntimeException("Customer status must be ACTIVE to create account");
        }

        Account account = new Account();
        account.setCustomer(customer);
        account.setType(req.type());
        account.setCurrency(req.currency());

        //account number is set here
        account.setAccountNumber(generateUniqueAccountNumber());

        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.ACTIVE);

        Account savedAccount = accountRepo.save(account);
        return toResponse(savedAccount);
    }

    @Transactional
    public List<AccountResponse> list(UUID customerId) {
        List<Account> accounts = (customerId == null)
                ? accountRepo.findAll() : accountRepo.findByCustomer_Id(customerId);

        return accounts.stream().map(this::toResponse).toList();
    }


    @Transactional
    public AccountResponse get(UUID accountId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(()-> new RuntimeException("Account not found"));

        return toResponse(account);
    }

    @Transactional
    public AccountResponse updateStatus(UUID accountId, UpdateAccountRequest req) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(()-> new RuntimeException("Account not found"));

        account.setStatus(req.status());

        Account saved = accountRepo.save(account);

        return toResponse(saved);
    }

 // ----------------------- Helpers -------------------------


    private String generateUniqueAccountNumber() {
        // Example: 12 digits.  can change the format later.
        // We loop until we find a number not in DB.
        String number;
        do {
            number = randomDigits(12);
        } while (accountRepo.existsByAccountNumber(number));
        return number;
    }


    private String randomDigits(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(secureRandom.nextInt(10));
        }
        return sb.toString();

    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getCustomer().getId(),
                account.getAccountNumber(),
                account.getType(),
                account.getCurrency(),
                account.getBalance(),
                account.getStatus(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }
}
