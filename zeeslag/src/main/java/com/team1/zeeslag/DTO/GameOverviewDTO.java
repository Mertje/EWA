package com.team1.zeeslag.DTO;

public record GameOverviewDTO(
    Long id,
    String loggedInUser,
    String playingAgainst
) {}
