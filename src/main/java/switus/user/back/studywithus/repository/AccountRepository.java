package switus.user.back.studywithus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import switus.user.back.studywithus.domain.account.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    @Query("SELECT a FROM Account a WHERE a.id IN (:accountIds) and a.delFlag = false")
    List<Account> findAllById(@Param("accountIds") List<Long> accountIds);
}
