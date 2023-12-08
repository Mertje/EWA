package com.team1.zeeslag.DTO;

import java.util.List;

public record GameAllDataDTO(
    Long id,
    List<String> userNames
) {}
