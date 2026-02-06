package seniorproject.bankifycore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seniorproject.bankifycore.domain.ClientUser;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface ClientUserRepository extends JpaRepository<ClientUser, UUID> {
    Optional<ClientUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
