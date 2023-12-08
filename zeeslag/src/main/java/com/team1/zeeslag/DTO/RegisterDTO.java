package com.team1.zeeslag.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record RegisterDTO(
    @Email(message = "Email should be valid, retry it.")
    String email,

    @NotEmpty(message = "Username shouldn't be empty.")
    String username,

    @NotEmpty(message = "Password shouldn't be empty.")
    String password
) {}
