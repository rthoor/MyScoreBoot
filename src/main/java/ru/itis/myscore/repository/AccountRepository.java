package ru.itis.myscore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.myscore.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByConfirmCode(String confirmCode);
}
