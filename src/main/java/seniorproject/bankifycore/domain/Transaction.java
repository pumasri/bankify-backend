package seniorproject.bankifycore.domain;

import jakarta.persistence.*;
import lombok.*;
import seniorproject.bankifycore.domain.base.Auditable;
import seniorproject.bankifycore.domain.enums.TransactionStatus;
import seniorproject.bankifycore.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transactions", indexes = {
                @Index(name = "idx_tx_from_account", columnList = "from_account_id"),
                @Index(name = "idx_tx_to_account", columnList = "to_account_id"),
                @Index(name = "idx_tx_created_at", columnList = "created_at")
})
public class Transaction extends Auditable {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 16)
        private TransactionType type;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 16)
        private TransactionStatus status = TransactionStatus.PENDING;

        @Column(nullable = false, precision = 18, scale = 2)
        private BigDecimal amount;

        // Nullable for DEPOSIT
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "from_account_id")
        private Account fromAccount;

        // Nullable for WithDraw
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "to_account_id")
        private Account toAccount;

        // idenpotency key([partner sends; server enforeces unique)
        @Column(nullable = false, unique = true, length = 64)
        private String reference;

        @Column(length = 255)
        private String note;
}
