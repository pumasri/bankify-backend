package seniorproject.bankifycore.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import seniorproject.bankifycore.dto.admin.ResetPinRequest;
import seniorproject.bankifycore.service.AccountService;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AccountService accountService;

    @PatchMapping("/accounts/{accountId}/pin")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public void resetPin(@PathVariable UUID accountId, @RequestBody ResetPinRequest req) {
        accountService.resetPin(accountId, req);
    }

    // @PostMapping("/clients/{id}/approve")

}
