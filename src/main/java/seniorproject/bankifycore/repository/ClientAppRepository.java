package seniorproject.bankifycore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seniorproject.bankifycore.domain.ClientApp;
import seniorproject.bankifycore.domain.enums.ClientStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientAppRepository extends JpaRepository<ClientApp, UUID> {
    Optional<ClientApp> findByApiKeyHashAndStatus(String apiKeyHash, ClientStatus status);

    boolean existsByApiKeyHash(String apiKeyHash);

    Optional<ClientApp> findByApiKeyHash(String apiKeyHash);

    List<ClientApp> findByStatus(ClientStatus status);
}
