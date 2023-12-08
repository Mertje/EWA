package com.team1.zeeslag.repository;

import com.team1.zeeslag.model.Board;
import com.team1.zeeslag.model.Square;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SquareRepository extends CrudRepository<Square, Long> {

    Optional<Square> findByIdAndBoard(Long id, Board board);
}
