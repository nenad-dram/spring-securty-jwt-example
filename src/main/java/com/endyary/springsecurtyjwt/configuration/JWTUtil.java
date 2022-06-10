package com.endyary.springsecurtyjwt.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

    // Token valid for 6h
    public static final long JWT_TOKEN_VALIDITY = 6 * 60 * 60;

    private final Map<Integer, String> jwtBlacklist = new HashMap<>();

    public String generateToken(String username) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withIssuer("JTW_EXAMPLE")
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .sign(Algorithm.HMAC256(secret));
    }

    public boolean isTokenValid(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exc) {
            return false;
        }
    }

    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }

    public boolean isTokenBlacklisted(String token) {
        return jwtBlacklist.containsKey(token.hashCode());
    }

    public void addToBlacklist(String token) {
        jwtBlacklist.put(token.hashCode(), token);
    }
}
