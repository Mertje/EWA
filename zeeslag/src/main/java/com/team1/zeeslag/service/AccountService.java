package com.team1.zeeslag.service;


import com.team1.zeeslag.exception.NoElementFoundException;
import com.team1.zeeslag.model.Account;
import com.team1.zeeslag.repository.AccountRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<String> getUsersStartingFrom(String email, int position) {
        Pageable pageable = PageRequest.of(position, 100, Sort.by("username"));
        List<String> listOfUsernames = new ArrayList<>();

        accountRepository.findAll(pageable).forEach(account -> {
            if (account.getEmail().equals(email)) return;
            listOfUsernames.add(account.getUsername());
        });

        return listOfUsernames;
    }

    public List<String> getAllUsersContainingString(String username, String emailAdres) {
        List<Account> accountList = accountRepository.findByUsernameContaining(username).orElseThrow(() ->
            new NoElementFoundException("No accounts where found")
        );

        List<String> users = new ArrayList<>();

        for (Account account : accountList) {
            if(!account.getEmail().equals(emailAdres)) {
                users.add(account.getUsername());
            }
        }

        if (users.isEmpty()) {
            throw new NoElementFoundException("No accounts where found by the name you looking for");
        }

        return users;
    }
}
