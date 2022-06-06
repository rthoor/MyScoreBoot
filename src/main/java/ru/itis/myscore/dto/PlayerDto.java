package ru.itis.myscore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.itis.myscore.model.Player;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Игрок")
public class PlayerDto {
    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "Имя", example = "Артур")
    @NotBlank(message = "Имя не может быть пустым")
    @NotNull(message = "Имя должно быть указано")
    private String firstName;

    @Schema(description = "Фамилия", example = "Бадамшин")
    @NotBlank(message = "Фамилия не может быть пустой")
    @NotNull(message = "Фамилия должна быть указана")
    private String lastName;

    @Schema(description = "Дата Рождения", example = "2001-07-15")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Дата Рождения должна быть указана")
    private Date dateOfBirth;

    @Schema(description = "Игровой номер", example = "11")
    @NotNull(message = "Игровой номер должен быть указан")
    @Min(0) @Max(99)
    private Integer number;

    @Schema(description = "ID страны", example = "2")
    private Long countryId;

    @Schema(description = "ID команды", example = "3")
    private Long teamId;

    @Schema(description = "Позиция", example = "Forward")
    @NotBlank(message = "Позиция не может быть пустой")
    @NotNull(message = "Позиция должна быть указана")
    private String position;

    public static PlayerDto from(Player player){
        return PlayerDto.builder()
                .id(player.getId())
                .firstName(player.getFirstName())
                .lastName(player.getLastName())
                .dateOfBirth(player.getDateOfBirth())
                .position(player.getPosition().name())
                .teamId(player.getTeam().getId())
                .countryId(player.getCountry().getId())
                .build();
    }

    public static List<PlayerDto> from(List<Player> players){
        return players.stream()
                .map(PlayerDto::from)
                .collect(Collectors.toList());
    }
}
