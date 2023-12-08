package com.team1.zeeslag.repository;

import com.team1.zeeslag.model.Board;
import com.team1.zeeslag.model.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface BoardRepository extends CrudRepository<Board, Long> {

    Optional<Board> findByAccountIdAndGameId(Long accountId, Long gameId);

    Optional<Board> findByGameAndAccountId(Game game, Long accountId);
}
