package com.team1.zeeslag.service;

import com.team1.zeeslag.DTO.*;
import com.team1.zeeslag.enumerate.Type;
import com.team1.zeeslag.exception.ElementFoundException;
import com.team1.zeeslag.exception.NoElementFoundException;
import com.team1.zeeslag.exception.RequestBodyException;
import com.team1.zeeslag.model.*;
import com.team1.zeeslag.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@AllArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final AccountRepository accountRepository;
    private final BoardRepository boardRepository;
    private final ShipRepository shipRepository;
    private final SquareRepository squareRepository;

    public GameIdentityDTO newGame(String emailFromToken, String username) {
        Account player2 = findAccountByUsername(username);
        Account player1 = findAccountByEmail(emailFromToken);

        List<Account> playersList = new ArrayList<>(Arrays.asList(player1, player2));

        checkGameAlreadyExists(playersList);

        Game game = new Game();
        game.setPlayersList(playersList);

        playersList.forEach(player -> {
            Board uniquePlayerBoard = createBasicBoard(game, player.getId());
            game.getPlayerBoards().add(uniquePlayerBoard);

            this.accountRepository.save(player);
        });

        game.getPlayerBoards().forEach(board -> {
            List<Square> squares = initSquares(board);
            this.squareRepository.saveAll(squares);

            List<Ship> ships = createShips(board);
            this.shipRepository.saveAll(ships);

            board.setSquares(squares);
            board.setShips(ships);
            this.boardRepository.save(board);
        });

        game.setTurnPlayerID(player1.getId());

        this.gameRepository.save(game);

        var a = game.getId();
        return new GameIdentityDTO(a);
    }

    public GameDataDTO getGameDetails(Long gameId, String email) {
        Game game = getGameById(gameId);
        checkWinner(game);

        Long requestPlayerID = findAccountByEmail(email).getId();

        Account oppositePlayer = getOppositePlayer(game, requestPlayerID);

        Board requestPlayerboard = getRequestPlayerboard(requestPlayerID, gameId);
        Board oppositePlayerboard = getRequestPlayerboard(oppositePlayer.getId(), gameId);

        List<Square> attackedSquares = getAttackedSquares(oppositePlayerboard);

        GameDataOppositePlayerDTO gameDataOppositePlayerDTO = new GameDataOppositePlayerDTO(
            oppositePlayerboard.getId(),
            attackedSquares,
            oppositePlayerboard.getShips().stream().allMatch(Ship::getPlaced)
        );

        return new GameDataDTO(
            game.getId(),
            game.getTurnPlayerID(),
            requestPlayerID,
            game.getPlayersList(),
            requestPlayerboard,
            gameDataOppositePlayerDTO
        );
    }

    public List<GameAllDataDTO> getAllGamesForUser(String email) {
        Account user = findAccountByEmail(email);
        List<Game> gamesAvailable = this.gameRepository.findGamesByPlayersListIn(List.of(user))
            .orElseThrow(() -> new NoElementFoundException("There are no games available"));

        return gamesAvailable.stream()
            .map(game -> {
                if (game.getWinnerUsername() == null) {
                   return new GameAllDataDTO(
                        game.getId(),
                        game.getPlayersList().stream()
                            .map(Account::getUsername)
                            .toList()
                   );
                }
                return null;
            })
            .filter(Objects::nonNull)
            .toList();
    }

    public String placeAllBoats(List<GamePlacedShipsDTO> ships, Long gameID, String email) {
        if (ships.size() != 5) {
            throw new NoElementFoundException("Provide exactly 5 ships");
        };

        Game game = getGameById(gameID);

        Account account = game.getPlayersList()
            .stream()
            .filter(player -> player.getEmail().equals(email))
            .findFirst()
            .orElseThrow(() -> new NoElementFoundException("Account not found in this game"));

        Board board = this.boardRepository.findByGameAndAccountId(game, account.getId()).get();

        if (board.getShips().stream().anyMatch(Ship::getPlaced)) {
            throw new ElementFoundException("The ships are already placed");
        }

        ships.forEach(ship -> {
            Ship shipInDb = getShipsFromRepository(ship, board);

            checkLengtOfShip(ship, shipInDb);

            ship.placedSquares().forEach(placedSquareId -> {
                Square square = getSquaresFromRepository(placedSquareId, board);
                square.setShip(shipInDb);
                square.setShipID(shipInDb.getId());
                this.squareRepository.save(square);
            });

            shipInDb.setPlaced(true);
            this.shipRepository.save(shipInDb);
        });

        return "success";
    }

    public String attackPosition(AttackSquareDTO SquareID, long gameId, String email) {

        Long attackedSquareID = SquareID.attackedSquareId();
        Game game = getGameById(gameId);
        List<Board> playerBoards = game.getPlayerBoards();
        Account userAccount = findAccountByEmail(email);

        checkWinner(game);

        if (!game.getPlayersList().contains(userAccount)) {
            throw new NoElementFoundException("Account is not in this game");
        }

        Board oppositePlayerBoard = playerBoards
            .stream()
            .filter(board -> !Objects.equals(board.getAccountId(), userAccount.getId())).findFirst()
            .get();

        playerBoards.forEach(playerBoard -> {
            if (!playerBoard.getShips().get(0).getPlaced()) {
                throw new NoElementFoundException("Not all ships are placed yet");
            }
        });

        if(!game.getTurnPlayerID().equals(userAccount.getId())) {
            throw new NoElementFoundException("Its the turn of the opposite player");
        }

        Square squareById = getSquaresFromRepository(attackedSquareID, oppositePlayerBoard);

        if(squareById.getAttacked()) {
            return "Already Hit";
        }

        squareById.setAttacked(true);
        game.setTurnPlayerID(oppositePlayerBoard.getAccountId());
        Ship shipOnSquare = squareById.getShip();

        this.squareRepository.save(squareById);

        if(shipOnSquare == null) {
            return "Miss";
        }

        shipOnSquare.setTimesHit(shipOnSquare.getTimesHit() + 1);

        if(shipOnSquare.getLength() == shipOnSquare.getTimesHit()) {
            shipOnSquare.setDestroyed(true);

            int destroyedCount = 0;

            for (Ship ship : oppositePlayerBoard.getShips()) {
                if (ship.getDestroyed()) {
                    destroyedCount++;
                }
            }

            if(destroyedCount == 5) {
                Account loser = this.accountRepository.findById(oppositePlayerBoard.getAccountId()).get();
                game.setWinnerUsername(userAccount.getUsername());
                this.gameRepository.save(game);
                return "Game over. " + loser.getUsername() + " Lost the game";
            }

            return "Destroyed ship: " + shipOnSquare.getId();
        }

        this.shipRepository.save(shipOnSquare);
        return "Hit";
    }

    public String surrender(long gameId, String email) {
        Game game = getGameById(gameId);
        checkWinner(game);

        Account surrenderedPlayer = findAccountByEmail(email);
        Account oppositePlayer = getOppositePlayer(game, surrenderedPlayer.getId());

        game.setWinnerUsername(oppositePlayer.getUsername());
        gameRepository.save(game);

        return surrenderedPlayer.getUsername() + " has surrendered";
    }

    private void checkLengtOfShip(GamePlacedShipsDTO ship, Ship shipInDb) {
        if(ship.placedSquares().size() != shipInDb.getLength()) {
            throw new RequestBodyException(
                "Ship id: " +
                    ship.shipId() +
                    ". is expected to have " +
                    shipInDb.getLength() +
                    " squares. the given amount of squares are " +
                    ship.placedSquares().size()
            );
        }
    }

    private List<Square> getAttackedSquares(Board oppositePlayerBoard) {
        return oppositePlayerBoard.getSquares().stream().map(square -> {
            Square copy = new Square();
            BeanUtils.copyProperties(square, copy);
            if (!square.getAttacked()) {
                copy.setShipID(null);
            }

            return copy;
        }).collect(Collectors.toList());
    }

    private Account getOppositePlayer(Game game, Long requestPlayerID) {
        return game.getPlayersList().stream()
            .filter(player -> !Objects.equals(player.getId(), requestPlayerID))
            .findFirst().orElse(null);
    }


    private void checkWinner(Game game) {
        if(game.getWinnerUsername() != null) {
            Account winnerAccount = findAccountByUsername(game.getWinnerUsername());
            throw new ElementFoundException("The game has ended with the winner as: " + winnerAccount.getUsername());
        }
    }

    private Square getSquaresFromRepository(Long squareID, Board board) {
        return this.squareRepository.findByIdAndBoard(squareID, board).orElseThrow(() ->
            new NoElementFoundException("The given square doesnt exist in this board"));
    }

    private Ship getShipsFromRepository(GamePlacedShipsDTO ship, Board board) {
        return this.shipRepository.findByIdAndBoard(ship.shipId(), board).orElseThrow(() ->
            new NoElementFoundException("The given ship id doesnt exist in this game"));
    }

    private Board getRequestPlayerboard(Long requestPlayerID, Long gameId) {
        return this.boardRepository.findByAccountIdAndGameId(requestPlayerID, gameId)
            .orElseThrow(() -> new NoElementFoundException("Given board for the account is not found"));
    }

    private Game getGameById(Long gameId) {
        return this.gameRepository.findById(gameId).orElseThrow(() ->
            new NoElementFoundException("The game doesn't exist"));
    }

    private List<Square> initSquares(Board board) {
        return IntStream.range(0, 100)
            .mapToObj(i -> Square.builder()
                .attacked(false)
                .board(board)
                .build())
            .collect(Collectors.toList());
    }

    private void checkGameAlreadyExists(List<Account> playersList) {
        this.gameRepository.findGamesByUsers(playersList.get(0).getId(), playersList.get(1).getId()).ifPresent((game) -> {
           if (game.getWinnerUsername() == null) {
               throw new ElementFoundException("You already have a game with this person: " + game.getId().toString());
           }
        });
    }

    private Account findAccountByEmail(String tokenUser) {
        return this.accountRepository.findByEmail(tokenUser).orElseThrow(
            () -> new NoElementFoundException("Something went wrong. Refresh the page."));
    }

    private Account findAccountByUsername(String username) {
        return this.accountRepository.findByUsername(username)
            .orElseThrow(() -> new NoElementFoundException("User not found with username: " + username));
    }

    private Board createBasicBoard(Game game, long playerID) {
        return Board.builder()
            .game(game)
            .accountId(playerID)
            .build();
    }

    private List<Ship> createShips(Board board) {
        List<Ship> ships = new ArrayList<>();

        for (Type type: Type.values()) {
            ships.add(
                Ship.builder()
                    .board(board)
                    .placed(false)
                    .destroyed(false)
                    .length(type.getNumericValue())
                    .build()
            );
        }
        return ships;
    }
}
