package seniorproject.bankifycore.domain;

import jakarta.persistence.*;
import lombok.*;
import seniorproject.bankifycore.domain.base.Auditable;
import seniorproject.bankifycore.domain.enums.AccountStatus;
import seniorproject.bankifycore.domain.enums.AccountType;
import seniorproject.bankifycore.domain.enums.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    @Column(nullable = false, unique = true, length = 32)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_app_id", unique = true)
    private ClientApp clientApp; // null for normal accounts


    @Column(name = "pin_hash")
    private String pinHash;

    @Column(name = "pin_locked_until")
    private Instant pinLockedUntil;

    @Column(name = "pin_failed_attempts", nullable = false)
    private int pinFailedAttempts;

    @Column(name = "pin_change_required", nullable = false)
    private boolean pinChangeRequired;

    @Version
    private int version;

    @PrePersist
    @PreUpdate
    private void validateOwnership() {
        if (customer == null && clientApp == null) {
            throw new IllegalStateException("Account must belong to either Customer or ClientApp");
        }
        if (customer != null && clientApp != null) {
            throw new IllegalStateException("Account cannot belong to both Customer and ClientApp");
        }
    }

}
