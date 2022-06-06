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
import ru.itis.myscore.dto.PlayerDto;
import ru.itis.myscore.service.PlayerService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    @Operation(summary = "Создание игрока")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Игрок",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema =
                                    @Schema(implementation = PlayerDto.class)
                            )
                    }
            )
    })
    @PostMapping
    public ResponseEntity<PlayerDto> postPlayer(@Valid @RequestBody PlayerDto playerDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(playerService.addPlayer(playerDto));
    }

    @Operation(summary = "Обновление игрока")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Игрок",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema =
                                    @Schema(implementation = PlayerDto.class)
                            )
                    }
            )
    })
    @PutMapping("/{player-id}")
    public ResponseEntity<PlayerDto> putPlayer(@Valid @Parameter(description = "ID игрока") @PathVariable("player-id") Long playerId,
                                                  @RequestBody PlayerDto playerDto) {
        playerDto.setId(playerId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(playerService.updatePlayer(playerDto));
    }

    @Operation(summary = "Получение игрока")
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
    @GetMapping("/{player-id}")
    public ResponseEntity<PlayerDto> getPlayer(@Parameter(description = "ID игрока") @PathVariable("player-id") Long playerId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playerService.getPlayer(playerId));
    }

    @Operation(summary = "Удаление игрока")
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
    @DeleteMapping("/{player-id}")
    public ResponseEntity<PlayerDto> deletePlayer(@Parameter(description = "ID игрока") @PathVariable("player-id") Long playerId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playerService.deletePlayer(playerId));
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
    public ResponseEntity<List<PlayerDto>> getAllPlayers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playerService.getAllPlayers());
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
    @GetMapping("")
    public ResponseEntity<List<PlayerDto>> getAllPlayers(@Parameter(description = "Имя игрока") @RequestParam("name") String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playerService.getPlayersByFirstNameOrLastName(name));
    }
}
