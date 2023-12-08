package com.team1.zeeslag.repository;

import com.team1.zeeslag.model.Account;
import com.team1.zeeslag.model.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface GameRepository extends CrudRepository<Game, Long> {

    Optional<List<Game>> findGamesByPlayersListIn(List<Account> players);

    @Query("SELECT g FROM Game g JOIN g.playersList p1 JOIN g.playersList p2 " +
        "WHERE p1.id = :userId1 AND p2.id = :userId2")
    Optional<Game> findGamesByUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);



    Game save(Game game);
}