package seniorproject.bankifycore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seniorproject.bankifycore.domain.PartnerKeyRotationRequest;
import seniorproject.bankifycore.domain.enums.RotationStatus;

import java.util.List;
import java.util.UUID;

@Repository
public interface RotationRepository extends JpaRepository<PartnerKeyRotationRequest, UUID> {
    boolean existsByPartnerApp_IdAndStatus(UUID partnerAppId, RotationStatus status);

    List<PartnerKeyRotationRequest> findByStatus(RotationStatus status);

    List<PartnerKeyRotationRequest> findByPartnerApp_IdOrderByCreatedAtDesc(UUID partnerAppId);

}
