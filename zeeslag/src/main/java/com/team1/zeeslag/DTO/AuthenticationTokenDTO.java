package com.team1.zeeslag.DTO;

public record AuthenticationTokenDTO(
    String token,
    String profilePictureUrl,
    String userName
) { }
