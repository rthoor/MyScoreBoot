package ru.itis.myscore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.myscore.dto.PlayerDto;
import ru.itis.myscore.dto.TeamDto;
import ru.itis.myscore.exception.CountryNotFoundException;
import ru.itis.myscore.exception.PlayerNotFoundException;
import ru.itis.myscore.exception.TeamNotFoundException;
import ru.itis.myscore.model.Country;
import ru.itis.myscore.model.Player;
import ru.itis.myscore.model.Team;
import ru.itis.myscore.repository.CountryRepository;
import ru.itis.myscore.repository.TeamRepository;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService{
    private final TeamRepository teamRepository;
    private final CountryRepository countryRepository;

    @Override
    public TeamDto addTeam(TeamDto teamDto) {
        Country country = countryRepository.findById(teamDto.getCountryId()).orElseThrow((Supplier<RuntimeException>) ()
                -> new CountryNotFoundException("Country with id: " + teamDto.getCountryId() + " not found"));
        return TeamDto.from(teamRepository.save(
                Team.builder()
                        .name(teamDto.getName())
                        .country(country)
                        .build()));
    }

    @Override
    public TeamDto getTeam(Long teamId) {
        return TeamDto.from(teamRepository.findById(teamId).orElseThrow((Supplier<RuntimeException>) ()
                -> new PlayerNotFoundException("Player with id: " + teamId + " not found")));
    }
}
