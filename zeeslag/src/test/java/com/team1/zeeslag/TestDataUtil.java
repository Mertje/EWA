package com.team1.zeeslag;


import com.team1.zeeslag.DTO.RegisterDTO;
import com.team1.zeeslag.model.Account;

public class TestDataUtil {
    private TestDataUtil() {}

    public static RegisterDTO createTestAccount() {
        return new RegisterDTO("test@me.nl", "testMe", "1234");
    }

    public static Account makeNewAccount() {
        return Account.builder()
            .email("test@me.nl")
            .username("tester")
            .password("12434")
            .build();
    }

    public static Account createTestAccount2() {
        return Account.builder()
            .email("test2@me.nl")
            .username("test2")
            .password("12434")
            .build();
    }

    public static Account createTestAccount3() {
        return Account.builder()
            .email("test3@me.nl")
            .username("test3")
            .password("12434")
            .build();
    }

}
