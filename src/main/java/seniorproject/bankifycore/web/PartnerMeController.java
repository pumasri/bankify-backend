package seniorproject.bankifycore.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import seniorproject.bankifycore.dto.partner.*;
import seniorproject.bankifycore.dto.transaction.TransactionResponse;
import seniorproject.bankifycore.service.partner.PartnerMeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/partner/me")
public class PartnerMeController {

    private final PartnerMeService partnerMeService;

    @GetMapping("/balance")
    public PartnerBalanceResponse balance() {
        return partnerMeService.balance();
    }

    @GetMapping("/transactions")
    public PartnerTransactionHistoryResponse history() {
        return partnerMeService.history();
    }

    @PostMapping("/transfer")
    public TransactionResponse transfer(
            @RequestHeader("Idempotency-Key") String idemKey,
            @RequestBody PartnerTransferRequest req
    ) {
        return partnerMeService.transfer(idemKey, req);
    }

    @PostMapping("/withdraw")
    public TransactionResponse withdraw(
            @RequestHeader("Idempotency-Key") String idemKey,
            @RequestBody PartnerWithdrawRequest req
    ) {
        return partnerMeService.withdraw(idemKey, req);
    }

    @PostMapping("/deposit")
    public TransactionResponse deposit(
            @RequestHeader("Idempotency-Key") String idemKey,
            @RequestBody PartnerDepositRequest req
    ) {
        return partnerMeService.deposit(idemKey, req);
    }



}
