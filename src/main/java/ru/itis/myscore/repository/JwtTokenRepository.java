package ru.itis.myscore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.myscore.model.JwtToken;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    boolean existsByToken(String token);
}
