package seniorproject.bankifycore.domain;

import jakarta.persistence.*;
import lombok.*;
import seniorproject.bankifycore.domain.base.Auditable;
import seniorproject.bankifycore.domain.enums.ClientStatus;

import java.util.UUID;

@Entity
@Table(name="client_apps",
    indexes = {
        @Index(name = "idx_client_apps_status",columnList = "status")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ClientApp extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 16)
    private ClientStatus status = ClientStatus.ACTIVE;

    //store the hash of the api
    @Column(name = "api_key_hash", nullable = false,unique = true,length = 128)
    private String apiKeyHash;
}
