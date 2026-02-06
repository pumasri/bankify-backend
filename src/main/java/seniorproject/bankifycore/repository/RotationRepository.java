package seniorproject.bankifycore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seniorproject.bankifycore.domain.ClientKeyRotationRequest;
import seniorproject.bankifycore.domain.enums.RotationStatus;

import java.util.List;
import java.util.UUID;

@Repository
public interface RotationRepository extends JpaRepository<ClientKeyRotationRequest, UUID> {
    boolean existsByClientApp_IdAndStatus(UUID clientAppId, RotationStatus status);
    List<ClientKeyRotationRequest> findByStatus(RotationStatus status);
    List<ClientKeyRotationRequest> findByClientApp_IdOrderByCreatedAtDesc(UUID clientAppId);


}
