package com.team1.zeeslag.service;

import com.team1.zeeslag.DTO.AuthenticationTokenDTO;
import com.team1.zeeslag.DTO.LoginDTO;
import com.team1.zeeslag.DTO.RegisterDTO;
import com.team1.zeeslag.TestDataUtil;
import com.team1.zeeslag.ZeeslagApplication;
import com.team1.zeeslag.exception.ElementFoundException;
import com.team1.zeeslag.exception.NoElementFoundException;
import com.team1.zeeslag.model.Account;
import com.team1.zeeslag.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AccountServiceIntegrationTest {

    private final AccountRepository accountRepository;
    private final AuthenticationService authenticationService;
    private final byte[] content = "This is a dummy file content".getBytes();
    private final MultipartFile file = new DummyMultipartFile("file", "dummy.txt", "text/plain", content);

    @Autowired
    public AccountServiceIntegrationTest(AccountRepository accountRepository, AuthenticationService authenticationService) {
        this.accountRepository = accountRepository;
        this.authenticationService = authenticationService;
    }

    @BeforeEach
    public void createAccount() {
        RegisterDTO userRegisterCredentials = TestDataUtil.createTestAccount();
        AuthenticationTokenDTO jwtToken = this.authenticationService.registerAccount(userRegisterCredentials, file);
        assertThat(jwtToken.token()).isNotNull();
    }

    @Test
    public void testThatAccountCanBeRecalled() {
        String userEmail = TestDataUtil.createTestAccount().email();
        Optional<Account> response = this.accountRepository.findByEmail(userEmail);

        assertThat(response).isPresent();
        assertThat(response.get().getEmail()).isEqualTo(userEmail);
    }

    @Test
    public void testLoginFunctionalityAfterRegister() {
        LoginDTO userLoginCredentials = new LoginDTO("test@me.nl", "1234");
        AuthenticationTokenDTO jwtToken = this.authenticationService.loginAccount(userLoginCredentials);

        assertThat(jwtToken.token()).isNotNull();
    }

    @Test
    public void testLoginFunctionalityAfterRegisterWithWrongPassword() {
        LoginDTO userLoginCredentials = new LoginDTO("test@me.nl", "12345");

        NoElementFoundException exception = assertThrows(NoElementFoundException.class, () ->
            this.authenticationService.loginAccount(userLoginCredentials)
        );

        String expectedErrorMessage = "Password is wrong";
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    public void testErrorHandlingWithDoubleEmail() {
        RegisterDTO userRegisterCredentialsWithSameEmail = TestDataUtil.createTestAccount();

        ElementFoundException exception = assertThrows(ElementFoundException.class, () ->
            this.authenticationService.registerAccount(userRegisterCredentialsWithSameEmail, file)
        );

        String expectedErrorMessage = "Sorry, email already exists";
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    public void testErrorHandlingWithDoubleUsername() {
        RegisterDTO userRegisterCredentialsWithSameUsername = new RegisterDTO("secondary@me.nl", "testMe", "1234");

        ElementFoundException exception = assertThrows(ElementFoundException.class, () ->
            authenticationService.registerAccount(userRegisterCredentialsWithSameUsername, file)
        );

        String expectedErrorMessage = "Sorry, username already exists";
        assertEquals(expectedErrorMessage, exception.getMessage());
    }


    private class DummyMultipartFile implements MultipartFile {

        private final byte[] content;
        private final String name;
        private final String originalFilename;
        private final String contentType;

        public DummyMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new java.io.ByteArrayInputStream(content);
        }

        @Override
        public Resource getResource() {
            return MultipartFile.super.getResource();
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {

        }

        @Override
        public void transferTo(java.nio.file.Path dest) throws IOException, IllegalStateException {
        }
    }

}
