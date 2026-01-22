package seniorproject.bankifycore.domain;

import jakarta.persistence.*;
import lombok.*;
import seniorproject.bankifycore.domain.base.Auditable;
import seniorproject.bankifycore.domain.enums.EntryDirection;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries", indexes = {
        @Index(name = "idx_ledger_account_created", columnList = "account_id,created_at"),
        @Index(name = "idx_ledger_tx", columnList = "transaction_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LedgerEntry extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EntryDirection direction;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;
}
