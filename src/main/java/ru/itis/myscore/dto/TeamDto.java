package ru.itis.myscore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.myscore.model.Team;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Команда")
public class TeamDto {
    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "Название", example = "Рубин")
    @NotBlank(message = "Название не может быть пустым")
    @NotNull(message = "Название должно быть указано")
    private String name;

    @Schema(description = "ID страны", example = "2")
    private Long countryId;

    public static TeamDto from(Team team){
        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .countryId(team.getCountry().getId())
                .build();
    }

    public static List<TeamDto> from(List<Team> teams){
        return teams.stream()
                .map(TeamDto::from)
                .collect(Collectors.toList());
    }
}
