package com.team1.zeeslag.controller;

import com.team1.zeeslag.DTO.AuthenticationTokenDTO;
import com.team1.zeeslag.DTO.LoginDTO;
import com.team1.zeeslag.DTO.RegisterDTO;
import com.team1.zeeslag.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController {

   private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Register an account and receive an token")
    @PostMapping(value = "/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AuthenticationTokenDTO> register(
        @Valid @ModelAttribute RegisterDTO account,
        @RequestParam(value = "file", required = false) MultipartFile file
    ) {
           AuthenticationTokenDTO token = authenticationService.registerAccount(account, file);
           return ResponseEntity.ok(token);
    }

    @Operation(summary = "Login to an account and receive a token")
    @PostMapping(value = "/login")
    public ResponseEntity<AuthenticationTokenDTO> login(@Valid @RequestBody LoginDTO account) {
            AuthenticationTokenDTO token = authenticationService.loginAccount(account);
            return ResponseEntity.ok(token);
    }
}
