package com.team1.zeeslag.service;


import com.team1.zeeslag.DTO.*;
import com.team1.zeeslag.TestDataUtil;
import com.team1.zeeslag.exception.ElementFoundException;
import com.team1.zeeslag.exception.NoElementFoundException;
import com.team1.zeeslag.exception.RequestBodyException;
import com.team1.zeeslag.model.*;
import com.team1.zeeslag.repository.AccountRepository;
import com.team1.zeeslag.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameServiceIntegrationTest {

    private final GameService gameService;
    private AccountRepository accountRepository;
    private GameRepository gameRepository;

    private final Account user1 = TestDataUtil.makeNewAccount();
    private final Account user2 = TestDataUtil.createTestAccount2();
    private final Account user3 = TestDataUtil.createTestAccount3();


    @Autowired
    public GameServiceIntegrationTest(GameService gameService, AccountRepository accountRepository, GameRepository gameRepository) {
        this.gameService = gameService;
        this.accountRepository = accountRepository;
        this.gameRepository = gameRepository;
    }


    @BeforeEach
    public void initAccounts() {
        this.accountRepository.saveAll(List.of(user1,user2,user3));
    }

    @Test
    public void testWhenGameDoesntExistThrowsError() {
        RuntimeException noGameError = assertThrows(NoElementFoundException.class, () ->
            this.gameService.getGameDetails(200L, "ARandomEmail@ga"));

        String errorMessage = "The game doesn't exist";
        assertEquals(errorMessage, noGameError.getMessage());
    }

    @Test
    public void testThatIfGameExistsWithSameUsersAnotherCantBeMade() {
        GameIdentityDTO game = this.gameService.newGame(user1.getEmail(), user2.getUsername());

        RuntimeException noGameError = assertThrows(ElementFoundException.class, () ->
            this.gameService.newGame(user1.getEmail(), user2.getUsername()) );

        String errorMessage = "You already have a game with this person: " + game.id();
        assertEquals(errorMessage, noGameError.getMessage());
    }


    @Test
    public void testThatGameDataIsReceivedAsTheDTO() {
        GameIdentityDTO gameIdentityDTO = this.gameService.newGame(user1.getEmail(), user2.getUsername());
        Long gameId = gameIdentityDTO.id();
        assertTrue(gameId != null);

        GameDataDTO gameDetails = this.gameService.getGameDetails(gameId, user1.getEmail());
        assertNotNull(gameDetails.oppositeBoard());
    }

    @Test
    public void testThatAccountsCanHaveMultipleGamesWithDifferentAccounts() {
        this.gameService.newGame(user1.getEmail(), user2.getUsername());
        this.gameService.newGame(user2.getEmail(), user3.getUsername());
        this.gameService.newGame(user1.getEmail(), user3.getUsername());

        Optional<List<Game>> gamesByPlayersListIn = this.gameRepository.findGamesByPlayersListIn(List.of(user1));

        assertTrue(gamesByPlayersListIn.isPresent());
        assertEquals(2, gamesByPlayersListIn.get().size());
    }

    @Test
    public void testThatAllGamesBelongingToAccountAreReceived() {
        this.gameService.newGame(user1.getEmail(), user2.getUsername());
        this.gameService.newGame(user1.getEmail(), user3.getUsername());

        List<GameAllDataDTO> allGamesForUser = this.gameService.getAllGamesForUser(user1.getEmail());

        assertNotNull(allGamesForUser);
        assertEquals(2, allGamesForUser.size());

    }

    @Test
    public void testThatAllBoatsArePlaced() {
        GameIdentityDTO gameIdentityDTO = this.gameService.newGame(user1.getEmail(), user2.getUsername());
        GameDataDTO gameDetails = this.gameService.getGameDetails(gameIdentityDTO.id(), user1.getEmail());
        Board board = gameDetails.userBoard();

        List<GamePlacedShipsDTO> listOfShips = new ArrayList<>(5);
        loopAndFillWithSquares(listOfShips, board);

        String success = this.gameService.placeAllBoats(listOfShips, gameIdentityDTO.id(), user1.getEmail());
        assertNotNull(success);
    }

    @Test
    public void testThatShipsCantBePlacedTwice() {
        GameIdentityDTO gameIdentityDTO = this.gameService.newGame(user1.getEmail(), user2.getUsername());
        GameDataDTO gameDetails = this.gameService.getGameDetails(gameIdentityDTO.id(), user1.getEmail());
        Board board = gameDetails.userBoard();

        List<GamePlacedShipsDTO> listOfShips = new ArrayList<>(5);
        loopAndFillWithSquares(listOfShips, board);

        this.gameService.placeAllBoats(listOfShips, gameIdentityDTO.id(), user1.getEmail());
        ElementFoundException noGameError = assertThrows(ElementFoundException.class, () ->
            this.gameService.placeAllBoats(listOfShips, gameIdentityDTO.id(), user1.getEmail()));

        String errorMessage = "The ships are already placed";
        assertEquals(errorMessage, noGameError.getMessage());
    }

    @Test
    public void testThatRandomShipIdCantBeInserted() {
        GameIdentityDTO gameIdentityDTO = this.gameService.newGame(user1.getEmail(), user2.getUsername());
        GameDataDTO gameDetails = this.gameService.getGameDetails(gameIdentityDTO.id(), user1.getEmail());
        Board board = gameDetails.userBoard();

        List<GamePlacedShipsDTO> listOfShips = new ArrayList<>(5);
        loopAndFillWithSquares(listOfShips,board);

        listOfShips.remove(4);
        listOfShips.add(new GamePlacedShipsDTO(212L, List.of(65L,54L,34L,32L)));

        String errorMessage = "The given ship id doesnt exist in this game";

        NoElementFoundException noShipIdError = assertThrows(NoElementFoundException.class, () ->
            this.gameService.placeAllBoats(listOfShips, gameIdentityDTO.id(), user1.getEmail())
        );

        assertEquals(errorMessage, noShipIdError.getMessage());
    }

    @Test
    public void testThatShipCantBeInsertedIfSquareIsNotUsers() {
        GameIdentityDTO gameIdentityDTO = this.gameService.newGame(user1.getEmail(), user2.getUsername());
        GameDataDTO gameDetails = this.gameService.getGameDetails(gameIdentityDTO.id(), user1.getEmail());
        Board board = gameDetails.userBoard();


        List<GamePlacedShipsDTO> listOfShips = new ArrayList<>(5);
        loopAndFillWithSquares(listOfShips, board);

        listOfShips.remove(4);
        listOfShips.add(new GamePlacedShipsDTO(5L, List.of(34L,130L)));

        String errorMessage = "The given square doesnt exist in this board";

        NoElementFoundException noShipIdError = assertThrows(NoElementFoundException.class, () ->
            this.gameService.placeAllBoats(listOfShips, gameIdentityDTO.id(), user1.getEmail())
        );

        assertEquals(errorMessage, noShipIdError.getMessage());
    }

    @Test
    public void testThatErrorWillBeThrownIfSquaresDontMatchUp() {
        GameIdentityDTO gameIdentityDTO = this.gameService.newGame(user1.getEmail(), user2.getUsername());
        GameDataDTO gameDetails = this.gameService.getGameDetails(gameIdentityDTO.id(), user1.getEmail());
        Board board = gameDetails.userBoard();


        List<GamePlacedShipsDTO> listOfShips = new ArrayList<>(5);
        loopAndFillWithSquares(listOfShips, board);

        listOfShips.remove(4);
        listOfShips.add(new GamePlacedShipsDTO(5L, List.of(34L,21l,31l,3131l)));

        String errorMessage = "Ship id: 5. is expected to have 2 squares. the given amount of squares are 4";

        RequestBodyException noShipIdError = assertThrows(RequestBodyException.class, () ->
            this.gameService.placeAllBoats(listOfShips, gameIdentityDTO.id(), user1.getEmail())
        );

        assertEquals(errorMessage, noShipIdError.getMessage());
    }

    @Test
    public void testThatNotExistingEmailWillThrowError() {
        String errorMessage = "Something went wrong. Refresh the page.";

        NoElementFoundException noShipIdError = assertThrows(NoElementFoundException.class, () ->
            this.gameService.getAllGamesForUser("aFakeEmail@gmail.com")
        );

        assertEquals(errorMessage, noShipIdError.getMessage());
    }

    @Test
    public void testThatErrorWillBeThrownIfArrayIsMissingShips() {
        GameIdentityDTO gameIdentityDTO = this.gameService.newGame(user1.getEmail(), user2.getUsername());
        GameDataDTO gameDetails = this.gameService.getGameDetails(gameIdentityDTO.id(), user1.getEmail());
        Board board = gameDetails.userBoard();

        List<GamePlacedShipsDTO> listOfShips = new ArrayList<>(5);
        loopAndFillWithSquares(listOfShips, board);
        listOfShips.remove(4);

        String errorMessage = "Provide exactly 5 ships";

        NoElementFoundException noShipIdError = assertThrows(NoElementFoundException.class, () ->
            this.gameService.placeAllBoats(listOfShips, gameIdentityDTO.id(), user1.getEmail())
        );

        assertEquals(errorMessage, noShipIdError.getMessage());
    }

    @Test
    public void testThatBoardWillThrowErrorIfNotFound() {
        GameIdentityDTO gameIdentityDTO = this.gameService.newGame(user1.getEmail(), user2.getUsername());

        String errorMessage = "Account is not in this game";

        NoElementFoundException noGameFoundError = assertThrows(NoElementFoundException.class, () ->
            this.gameService.attackPosition(new AttackSquareDTO(1L), gameIdentityDTO.id(), user3.getEmail() )
        );

        assertEquals(errorMessage, noGameFoundError.getMessage());
    }

    @Test
    public void testThatErrorWillBeThrownIfNoShipsArePlaced() {
        GameIdentityDTO gameIdentityDTO = this.gameService.newGame(user1.getEmail(), user2.getUsername());

        String errorMessage = "Not all ships are placed yet";

        NoElementFoundException missingShipsError = assertThrows(NoElementFoundException.class, () ->
            this.gameService.attackPosition(new AttackSquareDTO(1L), gameIdentityDTO.id(), user1.getEmail() )
        );

        assertEquals(errorMessage, missingShipsError.getMessage());
    }

    @Test
    public void testThatAttackIsSuccessfulHit() {
        GameIdentityDTO gameIdentityDTO = getGameIdentityDTO();

        String status = this.gameService.attackPosition(new AttackSquareDTO(101L), gameIdentityDTO.id(), user1.getEmail());
        assertEquals(status, "Hit");
    }

    @Test
    public void testThatAttackAtSameSpotIsIgnored() {
        GameIdentityDTO gameIdentityDTO = getGameIdentityDTO();
        String status = this.gameService.attackPosition(new AttackSquareDTO(101L), gameIdentityDTO.id(), user1.getEmail());
        assertEquals(status, "Hit");

        this.gameService.attackPosition(new AttackSquareDTO(1L), gameIdentityDTO.id(), user2.getEmail());

        String status2 = this.gameService.attackPosition(new AttackSquareDTO(101L), gameIdentityDTO.id(), user1.getEmail());
        assertEquals(status2, "Already Hit");
    }

    @Test
    public void testAttackNotPossibleIfNotTurn() {
        GameIdentityDTO gameIdentityDTO = getGameIdentityDTO();

        String errorMessage = "Its the turn of the opposite player";

        NoElementFoundException missingShipsError = assertThrows(NoElementFoundException.class, () ->
            this.gameService.attackPosition(new AttackSquareDTO(1L), gameIdentityDTO.id(), user2.getEmail())
        );

        assertEquals(errorMessage, missingShipsError.getMessage());
    }


    @Test
    public void testThatOtherUserCantPlayWhenNotHisTurn() {
        GameIdentityDTO gameIdentityDTO = getGameIdentityDTO();
        String status = this.gameService.attackPosition(new AttackSquareDTO(190L), gameIdentityDTO.id(), user1.getEmail());
        assertEquals(status, "Miss");

        String errorMessage = "Its the turn of the opposite player";

        NoElementFoundException missingShipsError = assertThrows(NoElementFoundException.class, () ->
            this.gameService.attackPosition(new AttackSquareDTO(123L), gameIdentityDTO.id(), user1.getEmail())
        );
        assertEquals(errorMessage, missingShipsError.getMessage());

    }

    @Test
    public void thatThatShipIsDestroyed() {
        GameIdentityDTO gameIdentityDTO = getGameIdentityDTO();
        this.gameService.attackPosition(new AttackSquareDTO(116L), gameIdentityDTO.id(), user1.getEmail());
        this.gameService.attackPosition(new AttackSquareDTO(50L), gameIdentityDTO.id(), user2.getEmail());
        String result = this.gameService.attackPosition(new AttackSquareDTO(117L), gameIdentityDTO.id(), user1.getEmail());

        assertEquals(result, "Destroyed ship: 10");
    }

    @Test
    public void testThatGameWillBeStoppedWhenSurrendered() {
        GameIdentityDTO gameIdentityDTO = this.gameService.newGame(user1.getEmail(), user2.getUsername());

        String surrenderMessage = this.gameService.surrender(gameIdentityDTO.id(), user1.getEmail());

        String surrenderCompareMessage = user1.getUsername() + " has surrendered";

        assertEquals(surrenderMessage, surrenderCompareMessage);

        String errorMessage = "The game has ended with the winner as: test2";
        ElementFoundException gameEndedError = assertThrows(ElementFoundException.class, () ->
            this.gameService.getGameDetails(gameIdentityDTO.id(), user1.getEmail())
        );
        assertEquals(errorMessage, gameEndedError.getMessage());
    }

    private void loopAndFillWithSquares(List<GamePlacedShipsDTO> listOfShips, Board board) {
        List<Ship> ships = board.getShips();
        List<Square> squares = board.getSquares();

        Long squareId = squares.stream().findFirst().get().getId();

        for (Ship ship: ships) {
            List<Long> arrayListOfLongs = new ArrayList<>();

            for (Long i = squareId; i < (squareId + ship.getLength()); i++) {
                arrayListOfLongs.add(i);
            }

            squareId = squareId + ship.getLength();
            listOfShips.add(new GamePlacedShipsDTO(ship.getId(), arrayListOfLongs));
        }
    }

    private GameIdentityDTO getGameIdentityDTO() {
        GameIdentityDTO gameIdentityDTO = this.gameService.newGame(user1.getEmail(), user2.getUsername());
        GameDataDTO gameDetails = this.gameService.getGameDetails(gameIdentityDTO.id(), user1.getEmail());
        Board board = gameDetails.userBoard();
        GameDataDTO gameDetails2 = this.gameService.getGameDetails(gameIdentityDTO.id(), user2.getEmail());
        Board board2 = gameDetails2.userBoard();

        List<GamePlacedShipsDTO> listOfShips = new ArrayList<>(5);
        loopAndFillWithSquares(listOfShips, board);

        List<GamePlacedShipsDTO> listOfShips2 = new ArrayList<>(5);
        loopAndFillWithSquares(listOfShips2, board2);

        this.gameService.placeAllBoats(listOfShips, gameIdentityDTO.id(), user1.getEmail());
        this.gameService.placeAllBoats(listOfShips2, gameIdentityDTO.id(), user2.getEmail());
        return gameIdentityDTO;
    }
}
