package seniorproject.bankifycore.web.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import seniorproject.bankifycore.consants.ApiPaths;
import seniorproject.bankifycore.dto.AuditLogResponse;
import seniorproject.bankifycore.dto.admin.ApproveClientResponse;
import seniorproject.bankifycore.dto.admin.ResetPinRequest;
import seniorproject.bankifycore.dto.clientapp.ClientAppResponse;
import seniorproject.bankifycore.dto.rotation.ApproveRotationResponse;
import seniorproject.bankifycore.dto.rotation.RejectRotationResponse;
import seniorproject.bankifycore.service.AccountService;
import seniorproject.bankifycore.service.AuditService;
import seniorproject.bankifycore.service.partner.ClientAppService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.ADMIN)
@RequiredArgsConstructor
public class AdminController {

    private final AccountService accountService;
    private final ClientAppService clientAppService;
    private final AuditService auditService;


    //reset pin
    @PatchMapping("/accounts/{accountId}/pin")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public void resetPin(@PathVariable UUID accountId, @RequestBody ResetPinRequest req) {
        accountService.resetPin(accountId, req);
    }

    // Clients
    @GetMapping("/clients")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public List<ClientAppResponse> listClients() {
        return clientAppService.list();
    }

    //disabling the account
    @PatchMapping("/clients/{id}/disable")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ClientAppResponse disable(@PathVariable UUID id) {
        return clientAppService.disable(id);
    }

    //activating the account
    @PatchMapping("/clients/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ClientAppResponse activate(@PathVariable UUID id) {
        return clientAppService.activate(id);
    }




    // approve API for client first time creating account
    @PatchMapping("/clients/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ApproveClientResponse approve(@PathVariable UUID id) {
        return clientAppService.approve(id);
    }

    // approving api rotation for client
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

    @GetMapping("/audit-logs")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public List<AuditLogResponse> listAuditLog(
            @RequestParam(required = false) String actorType,
            @RequestParam(required = false) String action) {
        return auditService.list(actorType, action);
    }

}
