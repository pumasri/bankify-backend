package seniorproject.bankifycore.domain;

import jakarta.persistence.*;
import lombok.*;
import seniorproject.bankifycore.domain.base.Auditable;

import java.util.UUID;

@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name="idx_audit_actor", columnList = "actor_type,actor_id"),
        @Index(name="idx_audit_action", columnList = "action"),
        @Index(name="idx_audit_entity", columnList = "entity_type,entity_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="actor_type", nullable=false, length=16)
    private String actorType; // USER / ATM / PARTNER

    @Column(name="actor_id", nullable=false, length=64)
    private String actorId;   // UUID as string (userId/accountId/partnerAppId)

    @Column(nullable=false, length=40)
    private String action;    // e.g. Partner_APPROVED, PIN_RESET, TX_DEPOSIT

    @Column(name="entity_type", nullable=false, length=40)
    private String entityType; // e.g. Partner, Account, Transaction

    @Column(name="entity_id", nullable=true, length=64)
    private String entityId;

    @Column(columnDefinition="TEXT")
    private String details; // json-ish string ok
}
