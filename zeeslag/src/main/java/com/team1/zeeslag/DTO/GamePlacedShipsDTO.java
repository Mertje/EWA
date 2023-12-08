package com.team1.zeeslag.DTO;

import java.util.List;

public record GamePlacedShipsDTO(
    Long shipId,
    List<Long> placedSquares
) { }
