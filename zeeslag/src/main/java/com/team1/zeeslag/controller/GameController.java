package com.team1.zeeslag.controller;

import com.team1.zeeslag.DTO.*;
import com.team1.zeeslag.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(
        summary = "create a new game",
        description = "Send the user name from the other guy. The game will be made if there is not an un going match"
    )
    @PostMapping(value = "")
    public ResponseEntity<GameIdentityDTO> newGame(
        @ModelAttribute("userToken") String userToken,
        @RequestBody newGameDTO user
    ) {
        GameIdentityDTO gameIdentityDTO = this.gameService.newGame(userToken, user.username());
        return ResponseEntity.ok(gameIdentityDTO);
    }

    @Operation(
        summary = "Enter game",
        description = "Enter the game that has been created, make sure to provide the auth bearer totken and the proper gameID"
    )
    @GetMapping(value = "/{gameId}")
    public ResponseEntity<GameDataDTO> gameDetails(
        @ModelAttribute("userToken") String userToken,
        @PathVariable Long gameId
    ) {
        GameDataDTO game = this.gameService.getGameDetails(gameId, userToken);
        return ResponseEntity.ok().body(game);
    }

    @Operation(
        summary = "All games for user",
        description = "Get all the games available for the user with the given bearer token"
    )
    @GetMapping(value = "")
    public ResponseEntity<List<GameAllDataDTO>> getAllGamesForUser(@ModelAttribute("userToken") String userToken) {
        List<GameAllDataDTO> games = this.gameService.getAllGamesForUser(userToken);
        return ResponseEntity.ok().body(games);
    }

    @Operation(
        summary = "place all ships",
        description = "Place all ships for the user, make sure all ships are provided at once, if the ships are already placed it throws an error"
    )
    @PutMapping(value = "/{gameId}/ship")
    public ResponseEntity<String> placeShips(
        @ModelAttribute("userToken") String userToken,
        @PathVariable Long gameId,
        @RequestBody List<GamePlacedShipsDTO> ships
    ) {
        String results = this.gameService.placeAllBoats(ships, gameId, userToken);
        return ResponseEntity.ok().body(results);
    }


    @PutMapping(value = "/{gameId}/attack")
    public ResponseEntity<String> attackShip(
        @ModelAttribute("userToken") String userToken,
        @PathVariable Long gameId,
        @RequestBody AttackSquareDTO attackedSquareId
    ) {
        String results = this.gameService.attackPosition(attackedSquareId, gameId, userToken);
        return ResponseEntity.ok().body(results);
    }

    @Operation(
        summary = "Surrender game",
        description = "Sets the winner of the game to be the opposite player," +
            "it returns an message and makes so that there can be a new game with the same users."
    )
    @GetMapping(value = "/{gameId}/surrender")
    public ResponseEntity<Map<String, Object>> surrender(
        @ModelAttribute("userToken") String userToken,
        @PathVariable Long gameId
    ) {
        String gameIdentityDTO = this.gameService.surrender(gameId, userToken);
        Map<String, Object> response = new HashMap<>();
        response.put("message", gameIdentityDTO);

        return ResponseEntity.ok(response);
    }

}
