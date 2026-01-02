package seniorproject.bankifycore.domain;

import jakarta.persistence.*;
import lombok.*;
import seniorproject.bankifycore.domain.base.Auditable;
import seniorproject.bankifycore.domain.enums.CustomerStatus;
import seniorproject.bankifycore.domain.enums.CustomerType;

import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

}
