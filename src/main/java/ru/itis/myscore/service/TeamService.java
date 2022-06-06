package ru.itis.myscore.service;

import ru.itis.myscore.dto.TeamDto;

import java.util.List;

public interface TeamService {
    TeamDto addTeam(TeamDto teamDto);
    TeamDto getTeam(Long teamId);
}
