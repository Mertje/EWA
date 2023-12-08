package com.team1.zeeslag.service;

import com.team1.zeeslag.DTO.AuthenticationTokenDTO;
import com.team1.zeeslag.DTO.LoginDTO;
import com.team1.zeeslag.DTO.RegisterDTO;
import com.team1.zeeslag.exception.ElementFoundException;
import com.team1.zeeslag.exception.NoElementFoundException;
import com.team1.zeeslag.model.Account;
import com.team1.zeeslag.repository.AccountRepository;
import com.team1.zeeslag.utility.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
@AllArgsConstructor
@Transactional
public class AuthenticationService {

    private final AccountRepository accountRepository;

    public AuthenticationTokenDTO registerAccount(RegisterDTO registerDTO, MultipartFile file) {
        String email = registerDTO.email();
        String userName = registerDTO.username();
        String rawPassword = registerDTO.password();

        checkEmailExist(email);
        checkUsernameExist(userName);

        String fileUrl = getFileUrl(file);
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        createAccount(email, userName, hashedPassword, fileUrl);

        String token = JwtUtil.generateToken(email);
        return new AuthenticationTokenDTO(token, fileUrl, userName);
    }

    public AuthenticationTokenDTO loginAccount(LoginDTO loginDTO) {
        String accountEmail = loginDTO.email();

        Account account = this.accountRepository.findByEmail(accountEmail)
            .orElseThrow(() -> new NoElementFoundException("The email doesn't have an account attached to it"));

        return tryLogin(loginDTO, account);
    }

    private String getFileUrl(MultipartFile file) {
        if (file == null) return "/defaults/default.jpg";
        String contentType = file.getContentType();

        if (contentType != null && (contentType.startsWith("image/jpeg") || contentType.startsWith("image/png"))) {
            String fileName = "profile_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String uploadDir = "src/main/resources/images";

            try {
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                return "/images/" + fileName;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "/defaults/default.jpg";
    }

    private void checkUsernameExist(String userName) {
        this.accountRepository.findByUsername(userName).ifPresent(account -> {
            throw new ElementFoundException("Sorry, username already exists");
        });
    }

    private void checkEmailExist(String email) {
        this.accountRepository.findByEmail(email).ifPresent(account -> {
            throw new ElementFoundException("Sorry, email already exists");
        });
    }


    private AuthenticationTokenDTO tryLogin(LoginDTO loginDTO, Account account) {
        if (BCrypt.checkpw(loginDTO.password(), account.getPassword())) {
            String token = JwtUtil.generateToken(account.getEmail());
            return  new AuthenticationTokenDTO(token, account.getProfilePictureUrl(), account.getUsername());
        }
        throw new NoElementFoundException("Password is wrong");
    }

    private void createAccount(String email, String userName, String hashedPassword, String profilePictureURL) {
        Account account = Account.builder()
            .email(email)
            .password(hashedPassword)
            .username(userName)
            .profilePictureUrl(profilePictureURL)
            .build();

        this.accountRepository.save(account);
    }
}
