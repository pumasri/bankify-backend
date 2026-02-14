package seniorproject.bankifycore.web.v1.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import seniorproject.bankifycore.consants.ApiPaths;
import seniorproject.bankifycore.dto.partner.*;
import seniorproject.bankifycore.dto.transaction.TransactionResponse;
import seniorproject.bankifycore.service.partner.PartnerApiService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.PARTNER + "/me")
public class PartnerMeController {

    private final PartnerApiService partnerMeService;

    @PreAuthorize("hasRole('PARTNER')")
    @GetMapping("/balance")
    public PartnerBalanceResponse balance() {
        return partnerMeService.balance();
    }

    @PreAuthorize("hasRole('PARTNER')")
    @GetMapping("/transactions")
    public PartnerTransactionHistoryResponse history() {
        return partnerMeService.history();
    }

    @PreAuthorize("hasRole('PARTNER')")
    @PostMapping("/transfer")
    public TransactionResponse transfer(
            @RequestHeader("Idempotency-Key") String idemKey,
            @RequestBody PartnerTransferRequest req) {
        return partnerMeService.transfer(idemKey, req);
    }

    @PreAuthorize("hasRole('PARTNER')")
    @PostMapping("/withdraw")
    public TransactionResponse withdraw(
            @RequestHeader("Idempotency-Key") String idemKey,
            @RequestBody PartnerWithdrawRequest req) {
        return partnerMeService.withdraw(idemKey, req);
    }

    @PreAuthorize("hasRole('PARTNER')")
    @PostMapping("/deposit")
    public TransactionResponse deposit(
            @RequestHeader("Idempotency-Key") String idemKey,
            @RequestBody PartnerDepositRequest req) {
        return partnerMeService.deposit(idemKey, req);
    }

}
