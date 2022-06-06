package ru.itis.myscore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.myscore.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
