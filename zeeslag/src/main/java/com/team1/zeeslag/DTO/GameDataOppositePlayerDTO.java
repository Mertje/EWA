package com.team1.zeeslag.DTO;

import com.team1.zeeslag.model.Square;

import java.util.List;


public record GameDataOppositePlayerDTO(
    Long id,
    List<Square> attackedSquares,
    Boolean shipsPlaced
) { }
