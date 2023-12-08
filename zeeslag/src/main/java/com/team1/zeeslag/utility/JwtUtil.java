package com.team1.zeeslag.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static String SECRET_KEY;

    private static long EXPIRATION;

    @Value("${jwt.secret}")
    private void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    @Value("${jwt.expiration}")
    private void setExpiration(long expiration) {
        EXPIRATION = expiration;
    }

    public static String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();
    }
}
