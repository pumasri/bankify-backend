package seniorproject.bankifycore.service.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seniorproject.bankifycore.domain.Account;
import seniorproject.bankifycore.repository.AccountRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartnerAccountService {

    private final AccountRepository accountRepo;
    private final PartnerAuthService partnerAuthService;

    @Transactional(readOnly = true)
    public Account getPartnerAccountOrThrow() {
        UUID clientId = partnerAuthService.currentClientId();
        return accountRepo.findByClientApp_Id(clientId)
                .orElseThrow(() -> new IllegalStateException("Partner account not found"));
    }
}
