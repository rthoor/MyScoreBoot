package ru.itis.myscore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.myscore.dto.MatchDto;
import ru.itis.myscore.dto.PlayerDto;
import ru.itis.myscore.exception.DateParseException;
import ru.itis.myscore.exception.TeamNotFoundException;
import ru.itis.myscore.model.Match;
import ru.itis.myscore.model.Player;
import ru.itis.myscore.model.Team;
import ru.itis.myscore.repository.MatchRepository;
import ru.itis.myscore.repository.TeamRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class MatchServiceImpl implements MatchService{
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    @Override
    public MatchDto addMatch(MatchDto matchDto) {
        Team homeTeam = teamRepository.findById(matchDto.getHomeTeamId()).orElseThrow((Supplier<RuntimeException>) ()
                -> new TeamNotFoundException("Team with id: " + matchDto.getHomeTeamId() + " not found"));
        Team awayTeam = teamRepository.findById(matchDto.getAwayTeamId()).orElseThrow((Supplier<RuntimeException>) ()
                -> new TeamNotFoundException("Team with id: " + matchDto.getAwayTeamId() + " not found"));
        return MatchDto.from(matchRepository.save(
                Match.builder()
                        .homeTeam(homeTeam)
                        .awayTeam(awayTeam)
                        .date(matchDto.getDate())
                        .goalsHome(matchDto.getGoalsHome())
                        .goalsAway(matchDto.getGoalsAway())
                        .build()));
    }

    @Override
    public List<MatchDto> getMatchesByDate(String dateAsString) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = formatter.parse(dateAsString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            Date endDate = calendar.getTime();

            return MatchDto.from(matchRepository.findMatchesByDateBetweenOrderByDateAsc(startDate, endDate));
        } catch (ParseException e) {
            throw new DateParseException("Неверный формат: " + dateAsString + "\n" + "Дата должна иметь формат dd-MM-yyyy");
        }
    }

    @Override
    public List<MatchDto> getMatchesByTeamId(Long teamId) {
        return MatchDto.from(matchRepository.findMatchesByTeamId(teamId));
    }
}
