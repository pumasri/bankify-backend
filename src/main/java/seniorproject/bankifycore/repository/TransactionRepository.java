package seniorproject.bankifycore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seniorproject.bankifycore.domain.Transaction;
import seniorproject.bankifycore.domain.enums.TransactionType;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByReference(String reference);

    boolean existsByReference(String reference);

    List<Transaction> findByFromAccount_IdOrToAccount_IdOrderByCreatedAtDesc(UUID fromId, UUID toId);

    List<Transaction> findByType(TransactionType type);

    List<Transaction> findByCreatedAtBetween(Instant from, Instant to);

    List<Transaction> findAllByOrderByCreatedAtDesc();
}
