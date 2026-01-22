package seniorproject.bankifycore.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seniorproject.bankifycore.domain.LedgerEntry;
import seniorproject.bankifycore.dto.ledger.LedgerEntryResponse;
import seniorproject.bankifycore.repository.LedgerEntryRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerEntryRepository ledgerEntryRepo;

    @Transactional
    public List<LedgerEntryResponse> listByAccount(UUID accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("accountId cannot be null");
        }
        List<LedgerEntry> accounts = ledgerEntryRepo.findByAccount_IdOrderByCreatedAtDesc(accountId);
        return accounts.stream().map(this::toResponse).toList();
    }

    public LedgerEntryResponse toResponse(LedgerEntry ledgerEntry) {
        return new LedgerEntryResponse(
                ledgerEntry.getId(),
                ledgerEntry.getTransaction().getId(),
                ledgerEntry.getDirection().toString(),
                ledgerEntry.getAmount(),
                ledgerEntry.getCurrency(),
                ledgerEntry.getCreatedAt());
    }
}
