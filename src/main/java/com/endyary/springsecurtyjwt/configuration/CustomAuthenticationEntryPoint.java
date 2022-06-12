package com.endyary.springsecurtyjwt.configuration;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles AuthenticationEntryPoint
 * <br>
 * When an unauthenticated user tries to access the secure/protected page,
 * Spring Security will throw an AuthenticationEntryPoint.
 * Security's configuration property authenticationEntryPoint
 * has to be configured to use this handler instance.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getOutputStream().println(authException.getMessage());
    }
}
