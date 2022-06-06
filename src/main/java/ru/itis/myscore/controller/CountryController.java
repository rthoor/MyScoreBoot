package ru.itis.myscore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.myscore.dto.CountryDto;
import ru.itis.myscore.dto.PlayerDto;
import ru.itis.myscore.service.CountryService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/countries")
public class CountryController {
    private final CountryService countryService;

    @Operation(summary = "Создание страны")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Страна",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema =
                                    @Schema(implementation = CountryDto.class)
                            )
                    }
            )
    })
    @PostMapping
    public ResponseEntity<CountryDto> postTeam(@Valid @RequestBody CountryDto countryDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(countryService.addCountry(countryDto));
    }

    @Operation(summary = "Получение всех игроков")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Игрок",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema =
                                    @Schema(implementation = PlayerDto.class)
                            )
                    }
            )
    })
    @GetMapping("/all")
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(countryService.getAllCountries());
    }
}
