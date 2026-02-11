package seniorproject.bankifycore.web.v1.atm;

import org.springframework.http.HttpStatus;
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
import seniorproject.bankifycore.consants.ApiPaths;
import seniorproject.bankifycore.dto.atm.*;
import seniorproject.bankifycore.dto.transaction.TransactionResponse;
import seniorproject.bankifycore.service.atm.AtmAuthService;
import seniorproject.bankifycore.service.atm.AtmMeService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.ATM)
public class AtmMeController {

    private final AtmMeService atmMeService;
    private final AtmAuthService atmAuthService;


    @PostMapping("/auth/login")
    public AtmLoginResponse login(@RequestBody AtmLoginRequest req) {
        return atmAuthService.login(req);
    }

    @PreAuthorize("hasRole('ATM')")
    @GetMapping("/me/balance")
    public AtmBalanceResponse balance() {
        return atmMeService.balance();
    }

    @PreAuthorize("hasRole('ATM')")
    @GetMapping("/me/transactions")
    public List<TransactionResponse> transactions(@RequestParam(required = false) Integer limit) {
        return atmMeService.transactions(limit);
    }

    @PreAuthorize("hasRole('ATM')")
    @PostMapping("/me/deposit")
    public TransactionResponse deposit(
            @RequestHeader("Idempotency-Key") String idemKey,
            @RequestBody AtmDepositRequest req) {
        return atmMeService.deposit(idemKey, req);
    }

    @PreAuthorize("hasRole('ATM')")
    @PostMapping("/me/withdraw")
    public TransactionResponse withdraw(
            @RequestHeader("Idempotency-Key") String idemKey,
            @RequestBody AtmWithdrawRequest req) {
        return atmMeService.withdraw(idemKey, req);
    }

    @PreAuthorize("hasRole('ATM')")
    @PostMapping("/me/transfer")
    public TransactionResponse transfer(
            @RequestHeader("Idempotency-Key") String idemKey,
            @RequestBody AtmTransferRequest req) {
        return atmMeService.transfer(idemKey, req);
    }

    @PreAuthorize("hasRole('ATM')")
    @PostMapping("/me/change-pin")
    public HttpStatus changePin(@RequestBody AtmChangePinRequest req) {
        atmMeService.changePin(req);
        return HttpStatus.NO_CONTENT;
    }

}
