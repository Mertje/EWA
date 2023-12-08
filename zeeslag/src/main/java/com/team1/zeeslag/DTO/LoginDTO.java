package com.team1.zeeslag.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginDTO(
    @Email(message = "Email should be valid")
    String email,

    @NotEmpty(message = "Password shouldn't be empty")
    String password

) {}