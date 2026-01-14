package seniorproject.bankifycore.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seniorproject.bankifycore.domain.Account;
import seniorproject.bankifycore.domain.Transaction;
import seniorproject.bankifycore.domain.enums.AccountStatus;
import seniorproject.bankifycore.domain.enums.TransactionStatus;
import seniorproject.bankifycore.domain.enums.TransactionType;
import seniorproject.bankifycore.dto.transaction.DepositRequest;
import seniorproject.bankifycore.dto.transaction.TransactionResponse;
import seniorproject.bankifycore.dto.transaction.TransferRequest;
import seniorproject.bankifycore.dto.transaction.WithdrawRequest;
import seniorproject.bankifycore.repository.AccountRepository;
import seniorproject.bankifycore.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;

    @Transactional
    public TransactionResponse deposit(DepositRequest request) {

        // check for repeated transaction idempotency
        idempotencyCheck(request.reference());

        // find the account | Check the account existence
        Account account = accountRepo.findById(request.accountId())
                .orElseThrow(() -> new IllegalArgumentException("Account Not found"));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }

        // Validating the amount of the
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Depositing amount must  be greater than zero");
        }

        // add the amount into the existing balance
        // Update the balance
        account.setBalance(account.getBalance().add(request.amount()));

        // Create a transaction and save transaction
        Transaction transaction = Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.SUCCESS)
                .amount(request.amount())
                .toAccount(account)
                .reference(request.reference())
                .note(request.note())
                .build();

        accountRepo.save(account);
        transactionRepo.save(transaction);

        return toResponse(transaction);
    }

    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {

        // check for repeated transaction
        idempotencyCheck(request.reference());

        Account account = accountRepo.findById(request.accountId())
                .orElseThrow(() -> new IllegalArgumentException("Account Not found"));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }

        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Withdrawing amount must  be greater than zero");
        }

        if (account.getBalance().compareTo(request.amount()) < 0) {
            throw new IllegalStateException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.amount()));

        Transaction transaction = Transaction.builder()
                .type(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.SUCCESS)
                .amount(request.amount())
                .fromAccount(account)
                .reference(request.reference())
                .note(request.note())
                .build();

        accountRepo.save(account);
        transactionRepo.save(transaction);

        return toResponse(transaction);
    }

    @Transactional
    public TransactionResponse transfer(TransferRequest request) {

        // check for repeated transaction idempotency
        idempotencyCheck(request.reference());

        if (request.fromAccountId().equals(request.toAccountId())) {
            throw new IllegalStateException("Cannot transfer to the same account");
        }

        Account fromAccount = accountRepo.findById(request.fromAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Sending Account Not found"));

        Account toAccount = accountRepo.findById(request.toAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Receiving Account Not found"));

        if (fromAccount.getStatus() != AccountStatus.ACTIVE || toAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Both Account must be active to transfer");
        }

        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Transfer amount must  be greater than zero");
        }

        if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new IllegalStateException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.amount()));
        toAccount.setBalance(toAccount.getBalance().add(request.amount()));

        Transaction transaction = Transaction.builder()
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.SUCCESS)
                .amount(request.amount())
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .reference(request.reference())
                .note(request.note())
                .build();

        accountRepo.save(fromAccount);
        accountRepo.save(toAccount);
        transactionRepo.save(transaction);
        return toResponse(transaction);
    }

    // idempotencyCheck method helper
    private void idempotencyCheck(String reference) {
        transactionRepo.findByReference(reference)
                .ifPresent(existingTransaction -> {
                    throw new IllegalStateException("Transaction already processed");
                });
    }

    // list by aacount id
    @Transactional
    public List<TransactionResponse> list(UUID accountId) {
        List<Transaction> transaction;
        if (accountId == null) {
            transaction = transactionRepo.findAllByOrderByCreatedAtDesc();
        } else {
            transaction = transactionRepo
                    .findByFromAccount_IdOrToAccount_IdOrderByCreatedAtDesc(accountId, accountId);
        }
        return transaction.stream().map(this::toResponse).toList();
    }

    @Transactional
    public TransactionResponse get(UUID transactionId) {
        Transaction transaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction Not found"));
        return toResponse(transaction);
    }

    // helper that help Entity -> DTO
    public TransactionResponse toResponse(Transaction transaction) {

        UUID fromId = transaction.getFromAccount() == null ? null : transaction.getFromAccount().getId();
        UUID toId = transaction.getToAccount() == null ? null : transaction.getToAccount().getId();

        return new TransactionResponse(
                transaction.getId(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getAmount(),
                fromId,
                toId,
                transaction.getReference(),
                transaction.getNote(),
                transaction.getCreatedAt());
    }
}
