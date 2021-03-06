package com.endyary.springsecurtyjwt.configuration;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.endyary.springsecurtyjwt.user.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that validates JWT and sets the authentication if the JWT is valid.
 * It will be executed before Spring's UsernamePasswordAuthenticationFilter
 */
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    private final HandlerExceptionResolver handlerExceptionResolver;

    public JWTFilter(UserService userService, JWTUtil jwtUtil,
                     HandlerExceptionResolver handlerExceptionResolver) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                // Get the JWT value (without the "Bearer " prefix)
                String jwt = authHeader.substring(7);

                // Validate the token
                if (!jwtUtil.isTokenValid(jwt) || jwtUtil.isTokenBlacklisted(jwt))
                    throw new JWTVerificationException("Invalid JWT token!");

                /*
                    If the token is valid set the user as authenticated.
                    This requires UserDetails object (caching is preferred instead of the DB access)
                 */
                UserDetails userDetails = userService.loadUserByUsername(jwtUtil.getSubject(jwt));
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (Exception exc) {
                // Handle an exception via ExceptionResolver and skip further processing
                handlerExceptionResolver.resolveException(request, response, null, exc);
                return;
            }
        }

        // Proceed to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
