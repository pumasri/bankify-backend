package seniorproject.bankifycore.web.v1.atm;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import lombok.RequiredArgsConstructor;
import seniorproject.bankifycore.dto.atm.*;
import seniorproject.bankifycore.dto.transaction.TransactionResponse;
import seniorproject.bankifycore.service.atm.AtmMeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/atm/me")
@PreAuthorize("hasRole('ATM')")
public class AtmMeController {

    private final AtmMeService atmMeService;

    @GetMapping("/balance")
    public AtmBalanceResponse balance() {
        return atmMeService.balance();
    }

    @GetMapping("/transactions")
    public List<TransactionResponse> transactions(@RequestParam(required = false) Integer limit) {
        return atmMeService.transactions(limit);
    }

    @PostMapping("/deposit")
    public TransactionResponse deposit(
            @RequestHeader("Idempotency-Key") String idemKey,
            @RequestBody AtmDepositRequest req) {
        return atmMeService.deposit(idemKey, req);
    }

    @PostMapping("/withdraw")
    public TransactionResponse withdraw(
            @RequestHeader("Idempotency-Key") String idemKey,
            @RequestBody AtmWithdrawRequest req) {
        return atmMeService.withdraw(idemKey, req);
    }

    @PostMapping("/transfer")
    public TransactionResponse transfer(
            @RequestHeader("Idempotency-Key") String idemKey,
            @RequestBody AtmTransferRequest req) {
        return atmMeService.transfer(idemKey, req);
    }

    @PostMapping("/change-pin")
    public void changePin(@RequestBody AtmChangePinRequest req) {
        atmMeService.changePin(req);
    }

}
