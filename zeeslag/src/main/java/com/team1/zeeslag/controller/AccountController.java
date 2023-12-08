package com.team1.zeeslag.controller;

import com.team1.zeeslag.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
        summary = "Get all usernames",
        description = "Start with 0 to get the first page of the usernames. You will receive a list of max 100 usernames to get to the second page enter 1 as page"
    )
    @GetMapping("/{page}")
    public ResponseEntity<List<String>> getAllUsersFromPosition(
        @ModelAttribute("userToken") String userToken,
        @PathVariable Integer page
    ) {
        List<String> results = accountService.getUsersStartingFrom(userToken, page);
        return ResponseEntity.ok(results);
    }

    @Operation(
        summary = "finds all users containing the given string",
        description = "give a string and receive the accounts containing that username"
    )
    @GetMapping("/user/{username}")
    public ResponseEntity<List<String>> findUsersContainingString(
        @ModelAttribute("userToken") String userToken,
        @PathVariable String username
    ) {
        List<String> results = accountService.getAllUsersContainingString(username, userToken);
        return ResponseEntity.ok(results);
    }
}
