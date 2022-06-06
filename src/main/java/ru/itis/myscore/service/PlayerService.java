package ru.itis.myscore.service;

import ru.itis.myscore.dto.PlayerDto;

import java.util.List;

public interface PlayerService {
    PlayerDto addPlayer(PlayerDto playerDto);
    PlayerDto updatePlayer(PlayerDto playerDto);
    PlayerDto getPlayer(Long playerId);
    PlayerDto deletePlayer(Long playerId);
    List<PlayerDto> getAllPlayers();
    List<PlayerDto> getPlayersByFirstNameOrLastName(String name);
}
