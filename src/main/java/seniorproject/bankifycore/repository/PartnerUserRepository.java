package seniorproject.bankifycore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seniorproject.bankifycore.domain.PartnerUser;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartnerUserRepository extends JpaRepository<PartnerUser, UUID> {
    Optional<PartnerUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
