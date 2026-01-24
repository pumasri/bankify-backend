package seniorproject.bankifycore.repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import seniorproject.bankifycore.domain.Account;
import seniorproject.bankifycore.domain.enums.AccountStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    List<Account> findByCustomer_Id(UUID accountNumbers);


    //When fetching this row, lock it in the database so no one else can modify it until I’m done.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.id = :id")
    Optional<Account>findByIdForUpdate(@Param("id") UUID id);

    Optional<Account> findByClientApp_Id(UUID clientAppId);

    Optional<Account> findFirstByCustomer_IdAndStatus(UUID customerId, AccountStatus status);

}
