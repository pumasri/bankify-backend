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
@Table(name = "partner_key_rotation_requests", indexes = {
                @Index(name = "idx_pkrr_status", columnList = "status"),
                @Index(name = "idx_pkrr_partner_app_id", columnList = "partner_app_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerKeyRotationRequest extends Auditable {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "partner_app_id", nullable = false)
        private PartnerApp partnerApp;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "requested_by_partner_user_id", nullable = false)
        private PartnerUser requestedBy;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 16)
        @Builder.Default
        private RotationStatus status = RotationStatus.PENDING;

        @Column(length = 300)
        private String reason;

}
