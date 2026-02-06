package seniorproject.bankifycore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seniorproject.bankifycore.domain.AuditLog;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByActorTypeAndAction(String actorType, String action);
    List<AuditLog> findByActorType(String actorType);
    List<AuditLog> findByAction(String action);


}
