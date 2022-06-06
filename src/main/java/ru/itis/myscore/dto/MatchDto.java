package ru.itis.myscore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.itis.myscore.model.Match;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Матч")
public class MatchDto {
    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "ID домашней команды", example = "2")
    @NotNull(message = "ID домашней команды не может быть пустым")
    private Long homeTeamId;

    @Schema(description = "ID гостевой команды", example = "3")
    @NotNull(message = "ID гостевой команды не может быть пустым")
    private Long awayTeamId;

    @Schema(description = "Количество голов, забитых домашней командой", example = "1")
    @Min(0)
    @NotNull(message = "Голы домашней команды быть указаны")
    private int goalsHome;

    @Schema(description = "Количество голов, забитых гостевой командой", example = "0")
    @Min(0)
    @NotNull(message = "Голы гостевой команды быть указаны")
    private int goalsAway;

    @Schema(description = "Дата и время матча", example = "2022-01-01 19:00:00")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Дата и время матча должны быть указаны")
    private Date date;

    public static MatchDto from(Match match){
        return MatchDto.builder()
                .id(match.getId())
                .homeTeamId(match.getHomeTeam().getId())
                .awayTeamId(match.getAwayTeam().getId())
                .date(match.getDate())
                .goalsHome(match.getGoalsHome())
                .goalsAway(match.getGoalsAway())
                .build();
    }

    public static List<MatchDto> from(List<Match> matches){
        return matches.stream()
                .map(MatchDto::from)
                .collect(Collectors.toList());
    }
}
