package seniorproject.bankifycore.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import seniorproject.bankifycore.dto.account.AccountResponse;
import seniorproject.bankifycore.dto.account.CreateAccountRequest;
import seniorproject.bankifycore.dto.account.UpdateAccountRequest;
import seniorproject.bankifycore.dto.ledger.LedgerEntryResponse;
import seniorproject.bankifycore.service.AccountService;
import seniorproject.bankifycore.service.LedgerService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

        private final AccountService accountService;
        private final LedgerService ledgerService;


        //POST   api/account     service.create()
        @PostMapping
        public AccountResponse create(@RequestBody CreateAccountRequest req){
            return accountService.create(req);
        }

        //GET /api/account/(id) -> service.list()
        @GetMapping
        public List<AccountResponse> list(@RequestParam(required = false) UUID customerId){
            return accountService.list(customerId);
        }

        //GET /api/account/{accountId}     -> service.get()
        @GetMapping("/{accountId}")
        public AccountResponse get(@PathVariable UUID accountId){
            return accountService.get(accountId);
        }


        //Pathc api/account/{id} -> service.updateStatus()
        @PatchMapping("/{accountId}")
        public AccountResponse update(@PathVariable UUID accountId, @RequestBody UpdateAccountRequest req){
            return accountService.updateStatus(accountId, req);
        }


        @GetMapping("/{accountId}/ledger")
        public List<LedgerEntryResponse> ledger(@PathVariable UUID accountId){
                return ledgerService.listByAccount(accountId);
        }
}
