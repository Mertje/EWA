package com.team1.zeeslag.utility;

import com.team1.zeeslag.DTO.AuthenticationTokenDTO;
import com.team1.zeeslag.DTO.LoginDTO;
import com.team1.zeeslag.DTO.RegisterDTO;
import com.team1.zeeslag.ZeeslagApplication;
import com.team1.zeeslag.service.AuthenticationService;
import com.team1.zeeslag.service.GameService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Profile("!prod")
@Component
public class databaseInitializer implements CommandLineRunner {

    // TODO remove this code once it is live
    private final AuthenticationService authenticationService;

    public databaseInitializer(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ZeeslagApplication.class, args);
    }


    byte[] content = "This is a dummy file content".getBytes();
    MultipartFile file = new databaseInitializer.DummyMultipartFile("file", "dummy.txt", "text/plain", content);

    @Override
    public void run(String... args)  {
        AuthenticationTokenDTO token = this.authenticationService.registerAccount(new RegisterDTO("m@m.nl", "me", "1234" ), file);
        System.out.println("Token: " + token.token());
        this.authenticationService.registerAccount(new RegisterDTO("e@m.nl", "mdwe", "1234"), file);
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
