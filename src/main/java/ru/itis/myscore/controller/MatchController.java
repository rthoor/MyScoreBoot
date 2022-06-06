package ru.itis.myscore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.myscore.dto.MatchDto;
import ru.itis.myscore.service.MatchService;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService;

    @Operation(summary = "Создание матча")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Матч",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema =
                                    @Schema(implementation = MatchDto.class)
                            )
                    }
            )
    })
    @PostMapping
    public ResponseEntity<MatchDto> postMatch(@Valid @RequestBody MatchDto matchDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(matchService.addMatch(matchDto));
    }

    @Operation(summary = "Получение всех матчей по календарному дню")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Матчи",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema =
                                    @Schema(implementation = MatchDto.class)
                            )
                    }
            )
    })
    @GetMapping("/{date}")
    public ResponseEntity<List<MatchDto>> getMatchesByDay(@Parameter(description = "Дата матча", example = "01-01-2022")
                                                              @PathVariable("date") String date) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(matchService.getMatchesByDate(date));
    }
}
