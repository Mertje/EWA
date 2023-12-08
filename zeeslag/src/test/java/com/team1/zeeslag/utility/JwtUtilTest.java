package com.team1.zeeslag.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class JwtUtilTest {

    private final String testerUserName = "TestUserName";

    @Test
    public void TestIfTokenIsGenerated() {
        String token = JwtUtil.generateToken(testerUserName);
        assertNotNull(token, "this object should not be null");
    }

    @Test
    public void TestIfTokenWillBeParsed() {
        String token = JwtUtil.generateToken(testerUserName);

        Claims claim = JwtUtil.parseToken(token);
        String subject = claim.getSubject();

        assertEquals(subject, testerUserName);
    }

    @Test
    public void TestIfErrorWithWrongToken() {
        MalformedJwtException malformedJwtException = assertThrows(MalformedJwtException.class, () ->
            JwtUtil.parseToken("A_Random_token_that_isnt_valid")
        );

        assertNotNull(malformedJwtException);
    }
}
