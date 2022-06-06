package ru.itis.myscore.service;

import ru.itis.myscore.dto.MatchDto;

import java.util.List;

public interface MatchService {
    MatchDto addMatch(MatchDto matchDto);
    List<MatchDto> getMatchesByDate(String dateAsString);
    List<MatchDto> getMatchesByTeamId(Long teamId);
}
