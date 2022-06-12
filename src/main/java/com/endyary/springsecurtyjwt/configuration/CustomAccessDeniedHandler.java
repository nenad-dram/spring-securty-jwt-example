package com.endyary.springsecurtyjwt.configuration;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles AccessDeniedException
 * <br>
 * When an unauthorized user tries to access the secure/protected page,
 * Spring Security will throw an AccessDeniedException.
 * Security's configuration property accessDeniedHandler
 * has to be configured to use this handler instance.
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(accessDeniedException.getMessage());
    }
}
