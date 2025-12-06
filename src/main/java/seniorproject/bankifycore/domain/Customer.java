package seniorproject.bankifycore.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seniorproject.bankifycore.domain.base.Auditable;

import java.util.UUID;

@Entity
@Table(name="customers")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    private String phoneNumber;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerType type;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;


    public enum CustomerType{INDIVIDUAL,BUSINESS}
    public enum Status{ACTIVE,FROZEN,CLOSED}

}
