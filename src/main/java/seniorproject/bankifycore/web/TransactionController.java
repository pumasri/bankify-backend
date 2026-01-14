package seniorproject.bankifycore.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import seniorproject.bankifycore.dto.transaction.DepositRequest;
import seniorproject.bankifycore.dto.transaction.TransactionResponse;
import seniorproject.bankifycore.dto.transaction.TransferRequest;
import seniorproject.bankifycore.dto.transaction.WithdrawRequest;
import seniorproject.bankifycore.service.TransactionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    // Deposit money into an account
    // API end point for top-ups
    @PostMapping("/deposit")
    public TransactionResponse deposit(@RequestBody DepositRequest depositRequest) {
        return transactionService.deposit(depositRequest);
    }

    // What: withdraw money from an account
    // Why: API endpoint for cash-out
    @PostMapping("/withdraw")
    public TransactionResponse withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        return transactionService.withdraw(withdrawRequest);
    }

    // What: transfer money between accounts
    // Why: API endpoint for moving money
    @PostMapping("/transfer")
    public TransactionResponse transfer(@RequestBody TransferRequest transferRequest) {
        return transactionService.transfer(transferRequest);
    }

    // What: list transactions for history/testing
    // Why: view ledger records (filters optional)
    @GetMapping
    public List<TransactionResponse> list(@RequestParam(required = false) UUID accountId) {
        return transactionService.list(accountId);
    }

    // What: get one transaction detail
    // Why: receipt/debugging
    @GetMapping("/{transactionId}")
    public TransactionResponse get(@PathVariable UUID transactionId) {
        return transactionService.get(transactionId);
    }

}

// account 1 id = 7452ea78-8c19-4b28-b081-29f2a3d1d275
// account 2 id = 24ba7741-e817-423c-b8dd-5275621da1e2
// account 3 id = 3e5bbb89-9d84-4185-84b4-a522e2bb1ead //frozen account