package seniorproject.bankifycore.domain;

import jakarta.persistence.*;
import lombok.*;
import seniorproject.bankifycore.domain.base.Auditable;
import seniorproject.bankifycore.domain.enums.PartnerUserRole;
import seniorproject.bankifycore.domain.enums.PartnerUserStatus;

import java.util.UUID;

@Entity
@Table(name = "partner_users", indexes = {
                @Index(name = "idx_partner_users_email", columnList = "email")
}, uniqueConstraints = {
                @UniqueConstraint(name = "uk_partner_users_email", columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerUser extends Auditable {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(nullable = false, length = 180)
        private String email;

        @Column(name = "password_hash", nullable = false, length = 255)
        private String passwordHash;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 16)
        @Builder.Default
        private PartnerUserStatus status = PartnerUserStatus.ACTIVE;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 16)
        @Builder.Default
        private PartnerUserRole role = PartnerUserRole.OWNER;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "partner_app_id", nullable = false)
        private PartnerApp partnerApp;
}
