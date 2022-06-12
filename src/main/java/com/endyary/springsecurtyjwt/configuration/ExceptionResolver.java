package com.endyary.springsecurtyjwt.configuration;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Global exception handler for all exceptions
 * except the Spring Security's ones
 */
@ControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<String> handleException(Exception e, HttpServletRequest request) {
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(e.getMessage());
    }
}
