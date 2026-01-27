package seniorproject.bankifycore.web.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import seniorproject.bankifycore.dto.admin.ApproveClientResponse;
import seniorproject.bankifycore.dto.clientapp.ClientAppResponse;

import seniorproject.bankifycore.service.partner.ClientAppService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/clients")
public class ClientAppController {

    private final ClientAppService clientAppService;

    // @PostMapping
    // @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    // public CreateClientAppResponse create(Authentication auth, @RequestBody
    // CreateClientAppRequest req) {
    // System.out.println("AUTH = " + auth);
    // System.out.println("AUTHORITIES = " + auth.getAuthorities());
    //
    // return clientAppService.create(req);
    // }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public List<ClientAppResponse> list() {
        return clientAppService.list();
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ClientAppResponse disable(@PathVariable UUID id) {
        return clientAppService.disable(id);
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ApproveClientResponse approve(@PathVariable UUID id) {
        return clientAppService.approve(id);
    }

}
