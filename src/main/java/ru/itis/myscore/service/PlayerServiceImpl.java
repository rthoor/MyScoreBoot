package ru.itis.myscore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.myscore.dto.PlayerDto;
import ru.itis.myscore.exception.CountryNotFoundException;
import ru.itis.myscore.exception.PlayerNotFoundException;
import ru.itis.myscore.exception.TeamNotFoundException;
import ru.itis.myscore.model.Country;
import ru.itis.myscore.model.Player;
import ru.itis.myscore.model.Team;
import ru.itis.myscore.repository.CountryRepository;
import ru.itis.myscore.repository.PlayerRepository;
import ru.itis.myscore.repository.TeamRepository;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class PlayerServiceImpl implements PlayerService{
    private final PlayerRepository playerRepository;
    private final CountryRepository countryRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public PlayerDto addPlayer(PlayerDto playerDto) {
        Country country = countryRepository.findById(playerDto.getCountryId()).orElseThrow((Supplier<RuntimeException>) ()
                -> new CountryNotFoundException("Country with id: " + playerDto.getCountryId() + " not found"));
        Team team = teamRepository.findById(playerDto.getTeamId()).orElseThrow((Supplier<RuntimeException>) ()
                -> new TeamNotFoundException("Team with id: " + playerDto.getTeamId() + " not found"));
        return PlayerDto.from(playerRepository.save(
                Player.builder()
                        .firstName(playerDto.getFirstName())
                        .lastName(playerDto.getLastName())
                        .dateOfBirth(playerDto.getDateOfBirth())
                        .number(playerDto.getNumber())
                        .team(team)
                        .country(country)
                        .position(Player.Position.parse(playerDto.getPosition()))
                        .build()));
    }

    @Override
    @Transactional
    public PlayerDto updatePlayer(PlayerDto playerDto) {
        Player player = playerRepository.findById(playerDto.getId()).orElseThrow((Supplier<RuntimeException>) ()
                -> new PlayerNotFoundException("Player with id: " + playerDto.getId() + " not found"));
        Country country = countryRepository.findById(playerDto.getCountryId()).orElseThrow((Supplier<RuntimeException>) ()
                -> new CountryNotFoundException("Country with id: " + playerDto.getCountryId() + " not found"));
        Team team = teamRepository.findById(playerDto.getTeamId()).orElseThrow((Supplier<RuntimeException>) ()
                -> new TeamNotFoundException("Team with id: " + playerDto.getTeamId() + " not found"));

        player.setCountry(country);
        player.setTeam(team);
        player.setFirstName(player.getFirstName());
        player.setLastName(player.getLastName());
        player.setDateOfBirth(playerDto.getDateOfBirth());
        player.setNumber(player.getNumber());
        player.setPosition(Player.Position.valueOf(playerDto.getPosition()));

        return PlayerDto.from(playerRepository.save(player));
    }

    @Override
    public PlayerDto getPlayer(Long playerId) {
        return PlayerDto.from(playerRepository.findById(playerId).orElseThrow((Supplier<RuntimeException>) ()
                -> new PlayerNotFoundException("Player with id: " + playerId + " not found")));
    }

    @Override
    public PlayerDto deletePlayer(Long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow((Supplier<RuntimeException>) ()
                -> new PlayerNotFoundException("Player with id: " + playerId + " not found"));
        playerRepository.delete(player);
        return PlayerDto.from(player);
    }

    @Override
    public List<PlayerDto> getAllPlayers() {
        return PlayerDto.from(playerRepository.findAll());
    }

    @Override
    public List<PlayerDto> getPlayersByFirstNameOrLastName(String name) {
        return PlayerDto.from
                (playerRepository.findPlayersByNameContains("%" + name.toLowerCase() + "%"));
    }
}
