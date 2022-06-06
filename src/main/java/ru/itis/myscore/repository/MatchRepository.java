package ru.itis.myscore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.myscore.model.Match;

import java.util.Date;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findMatchesByDateBetweenOrderByDateAsc(Date start, Date end);

    @Query("select match from Match match " +
            " where match.homeTeam.id = ?1 " +
            " or match.awayTeam.id = ?1 " +
            " order by match.date asc")
    List<Match> findMatchesByTeamId(Long teamId);
}
