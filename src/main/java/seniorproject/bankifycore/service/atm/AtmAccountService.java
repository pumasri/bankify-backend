package seniorproject.bankifycore.service.atm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seniorproject.bankifycore.domain.Account;
import seniorproject.bankifycore.domain.Customer;
import seniorproject.bankifycore.domain.enums.AccountStatus;
import seniorproject.bankifycore.domain.enums.CustomerStatus;
import seniorproject.bankifycore.repository.AccountRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtmAccountService {

    private final AtmAuthService atmAuthService;
    private final AccountRepository accountRepo;

    @Transactional(readOnly = true)
    public Account myActiveAccountOrThrow() {
        var userId = atmAuthService.currentAccountId();

        Account account = accountRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("ATM account not found"));



        // block partner settlement accounts from ATM
        if (account.getClientApp() != null) {
            throw new IllegalStateException("ATM account not found");
        }

        // ✅ Business rule 1: customer frozen blocks ATM
        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new IllegalStateException("Account is frozen");
        }

        Customer customer = account.getCustomer();
        if (customer == null) {
            throw new IllegalStateException("Customer not found for this ATM account");
        }

        if (account.isPinChangeRequired()) {
            throw new IllegalStateException("PIN change required");
        }

        // ✅ Business rule 2: account frozen blocks money ops (and balance, if you want)
        if (customer.getStatus() == CustomerStatus.FROZEN) {
            throw new IllegalStateException("Customer is frozen");
        }

        return account;
    }


    @Transactional(readOnly = true)
    public Account myAccountAllowPinChangeOrThrow() {
        UUID accountId = atmAuthService.currentAccountId();
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalStateException("ATM account not found"));

        if (account.getClientApp() != null) throw new IllegalStateException("ATM account not found");
        if (account.getStatus() == AccountStatus.FROZEN) throw new IllegalStateException("Account is frozen");
        if (account.getCustomer() == null) throw new IllegalStateException("Customer not found for this ATM account");
        if (account.getCustomer().getStatus() == CustomerStatus.FROZEN) throw new IllegalStateException("Customer is frozen");

        return account;
    }

}
