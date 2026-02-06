package seniorproject.bankifycore.dto;

import jakarta.persistence.Column;

public record AuditLogResponse(
        String actorType, // USER / ATM / PARTNER
        String actorId,  // UUID as string (userId/accountId/clientAppId)
        String action,    // e.g. CLIENT_APPROVED, PIN_RESET, TX_DEPOSIT
        String entityType, // e.g. ClientApp, Account, Transaction
        String entityId,
        String details // json-ish string ok
) {
}
