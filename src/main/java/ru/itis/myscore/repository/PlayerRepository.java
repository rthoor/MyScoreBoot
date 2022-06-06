package ru.itis.myscore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.myscore.model.Player;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Query("select player from Player player " +
            " where lower(player.firstName) like ?1 " +
            " or lower(player.lastName) like ?1 ")
    List<Player> findPlayersByNameContains(String name);
}
