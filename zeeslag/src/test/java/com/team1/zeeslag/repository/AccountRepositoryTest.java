package com.team1.zeeslag.repository;

import com.team1.zeeslag.TestDataUtil;
import com.team1.zeeslag.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AccountRepositoryTest {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountRepositoryTest(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @BeforeEach
    public void createDummyAccount() {
        Account userRegisterCredentials = TestDataUtil.makeNewAccount();
        this.accountRepository.save(userRegisterCredentials);
    }

    @Test
    public void testFindByEmail() {
        String email = TestDataUtil.makeNewAccount().getEmail();
        Optional<Account> response = this.accountRepository.findByEmail(email);

        assertThat(response).isPresent();
        assertThat(response.get().getEmail()).isEqualTo(email);
    }

    @Test
    public void testFindByUsername() {
        String username = TestDataUtil.makeNewAccount().getUsername();
        Optional<Account> response = this.accountRepository.findByUsername(username);

        assertThat(response).isPresent();
        assertThat(response.get().getUsername()).isEqualTo(username);
    }
}
