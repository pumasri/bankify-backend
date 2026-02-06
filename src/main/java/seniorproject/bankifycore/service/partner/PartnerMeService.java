package seniorproject.bankifycore.service.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seniorproject.bankifycore.domain.Account;
import seniorproject.bankifycore.dto.partner.*;
import seniorproject.bankifycore.dto.transaction.DepositRequest;
import seniorproject.bankifycore.dto.transaction.TransactionResponse;
import seniorproject.bankifycore.dto.transaction.TransferRequest;
import seniorproject.bankifycore.dto.transaction.WithdrawRequest;
import seniorproject.bankifycore.service.TransactionService;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerMeService {

    private final PartnerAccountService partnerAccountService;
    private final TransactionService transactionService;


    //get the partner balance
    @Transactional(readOnly = true)
    public PartnerBalanceResponse balance() {
        Account acc = partnerAccountService.getPartnerAccountOrThrow();
        return new PartnerBalanceResponse(
                acc.getId(),
                acc.getBalance(),
                String.valueOf(acc.getCurrency()), // change if currency is String/enum
                acc.getUpdatedAt() instanceof Instant i ? i : acc.getUpdatedAt() // adjust to your Auditable type
        );
    }

    //get the partner transacgtions
    @Transactional(readOnly = true)
    public PartnerTransactionHistoryResponse history() {
        Account acc = partnerAccountService.getPartnerAccountOrThrow();
        List<TransactionResponse> txs = transactionService.list(acc.getId());
        return new PartnerTransactionHistoryResponse(acc.getId(), txs);
    }


    //partner money transfer
    @Transactional
    public TransactionResponse transfer(String idemKey, PartnerTransferRequest req) {
        Account acc = partnerAccountService.getPartnerAccountOrThrow();
        TransferRequest serviceReq = new TransferRequest(acc.getId(), req.toAccountId(), req.amount(), req.note());
        return transactionService.transfer(idemKey, serviceReq);
    }


    //partner withdraw money
    @Transactional
    public TransactionResponse withdraw(String idemKey, PartnerWithdrawRequest req) {
        Account acc = partnerAccountService.getPartnerAccountOrThrow();
        WithdrawRequest serviceReq = new WithdrawRequest(acc.getId(), req.amount(), req.note());
        return transactionService.withdraw(idemKey, serviceReq);
    }


    //partner deposit money to his own account
    @Transactional
    public TransactionResponse deposit(String idemKey, PartnerDepositRequest req) {
        Account acc = partnerAccountService.getPartnerAccountOrThrow();
        DepositRequest serviceReq = new DepositRequest(acc.getId(), req.amount(), req.note());
        return transactionService.deposit(idemKey, serviceReq);
    }



}
