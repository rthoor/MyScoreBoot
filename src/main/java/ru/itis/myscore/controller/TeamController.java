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
import ru.itis.myscore.dto.TeamDto;
import ru.itis.myscore.service.MatchService;
import ru.itis.myscore.service.TeamService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;
    private final MatchService matchService;

    @Operation(summary = "Создание команды")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Команда",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema =
                                    @Schema(implementation = TeamDto.class)
                            )
                    }
            )
    })
    @PostMapping
    public ResponseEntity<TeamDto> postTeam(@Valid @RequestBody TeamDto teamDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(teamService.addTeam(teamDto));
    }

    @Operation(summary = "Получение команды")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команда",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema =
                                    @Schema(implementation = TeamDto.class)
                            )
                    }
            )
    })
    @GetMapping("/{team-id}")
    public ResponseEntity<TeamDto> getTeam(@Parameter(description = "ID команды") @PathVariable("team-id") Long teamId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(teamService.getTeam(teamId));
    }

    @Operation(summary = "Получение всех матчей команды")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команда",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema =
                                    @Schema(implementation = TeamDto.class)
                            )
                    }
            )
    })
    @GetMapping("/{team-id}/matches")
    public ResponseEntity<List<MatchDto>> getMatchesByTeam(@Parameter(description = "ID команды") @PathVariable("team-id") Long teamId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(matchService.getMatchesByTeamId(teamId));
    }
}
