package seniorproject.bankifycore.domain;

import jakarta.persistence.*;
import lombok.*;
import seniorproject.bankifycore.domain.base.Auditable;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter @Setter
@Table(name="accounts")
@NoArgsConstructor @AllArgsConstructor
public class Account extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false,unique = true,length = 32)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Column(nullable = false,length = 3)
    private String currency;

    @Column(nullable = false,precision = 18,scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;


    @Version
    private int version;

    public enum Status {ACTIVE, FROZEN,CURRENT}
    public enum AccountType {WALLET,SAVINGS,CURRENT}

}
