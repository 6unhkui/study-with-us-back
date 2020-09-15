package switus.user.back.studywithus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import switus.user.back.studywithus.domain.account.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
}
