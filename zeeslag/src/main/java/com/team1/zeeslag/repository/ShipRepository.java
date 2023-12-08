package com.team1.zeeslag.repository;

import com.team1.zeeslag.model.Board;
import com.team1.zeeslag.model.Game;
import com.team1.zeeslag.model.Ship;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ShipRepository extends CrudRepository<Ship, Long> {

    Optional<Ship> findByIdAndBoard(Long id, Board board);

}
