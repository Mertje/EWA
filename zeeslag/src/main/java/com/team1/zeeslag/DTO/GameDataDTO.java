package com.team1.zeeslag.DTO;

import com.team1.zeeslag.model.Account;
import com.team1.zeeslag.model.Board;

import java.util.List;

public record GameDataDTO(
    Long gameID,
    Long playerTurn,
    Long userID,
    List<Account> playerList,
    Board userBoard,
    GameDataOppositePlayerDTO oppositeBoard
) {}
