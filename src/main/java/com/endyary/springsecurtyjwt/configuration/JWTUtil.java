package com.endyary.springsecurtyjwt.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String username) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withIssuer("JTW_EXAMPLE")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndGetSubject(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }
}
