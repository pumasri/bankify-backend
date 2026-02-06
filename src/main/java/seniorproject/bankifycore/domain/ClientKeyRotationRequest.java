package seniorproject.bankifycore.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import seniorproject.bankifycore.domain.base.Auditable;
import seniorproject.bankifycore.domain.enums.RotationStatus;

import java.util.UUID;

@Entity
@Table(name = "client_key_rotation_requests",
        indexes = {
                @Index(name = "idx_ckrr_status", columnList = "status"),
                @Index(name = "idx_ckrr_client_app_id", columnList = "client_app_id")
        })
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ClientKeyRotationRequest extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_app_id", nullable = false)
    private ClientApp clientApp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requested_by_client_user_id", nullable = false)
    private ClientUser requestedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Builder.Default
    private RotationStatus status = RotationStatus.PENDING;

    @Column(length = 300)
    private String reason;

}
