package seniorproject.bankifycore.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import seniorproject.bankifycore.dto.ApproveRotationResponse;
import seniorproject.bankifycore.dto.RejectRotationResponse;
import seniorproject.bankifycore.dto.admin.ApproveClientResponse;
import seniorproject.bankifycore.dto.admin.ResetPinRequest;
import seniorproject.bankifycore.dto.clientapp.ClientAppResponse;
import seniorproject.bankifycore.service.AccountService;
import seniorproject.bankifycore.service.partner.ClientAppService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AccountService accountService;
    private final ClientAppService clientAppService;

    @PatchMapping("/accounts/{accountId}/pin")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public void resetPin(@PathVariable UUID accountId, @RequestBody ResetPinRequest req) {
        accountService.resetPin(accountId, req);
    }

    //Clients
    @GetMapping("/clients")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public List<ClientAppResponse> list() {
        return clientAppService.list();
    }

    @PatchMapping("/clients/{id}/disable")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ClientAppResponse disable(@PathVariable UUID id) {
        return clientAppService.disable(id);
    }

    //approve API for client first time creating account
    @PatchMapping("/clients/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ApproveClientResponse approve(@PathVariable UUID id) {
        return clientAppService.approve(id);
    }

    //approving api rotation for client
    @PatchMapping("/clients/rotation-requests/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ApproveRotationResponse approveRotation(@PathVariable UUID id) {
        return clientAppService.approveRotation(id);
    }

    @PatchMapping("/clients/rotation-requests/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public RejectRotationResponse rejectRotation(@PathVariable UUID id) {
        return clientAppService.reject(id);
    }




}
