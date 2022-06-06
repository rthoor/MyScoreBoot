package ru.itis.myscore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.myscore.model.Country;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Страна")
public class CountryDto {
    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "Название", example = "Россия")
    @NotBlank(message = "Название не может быть пустым")
    @NotNull(message = "Название должно быть указано")
    private String name;

    public static CountryDto from(Country country){
        return CountryDto.builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }

    public static List<CountryDto> from(List<Country> countries){
        return countries.stream()
                .map(CountryDto::from)
                .collect(Collectors.toList());
    }
}
