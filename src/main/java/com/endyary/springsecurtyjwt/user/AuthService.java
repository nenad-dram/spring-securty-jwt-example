package com.endyary.springsecurtyjwt.user;

import com.endyary.springsecurtyjwt.configuration.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthService {

    private final AuthenticationManager authManager;

    private final JWTUtil jwtUtil;

    public AuthService(AuthenticationManager authManager, JWTUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    public Authentication doAuth(String username, String password) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(username, password);

        //AuthenticationManager will use the configured UserDetailsService bean to perform the auth
        return authManager.authenticate(authInputToken);
    }

    public String generateToken(String username) {
        return jwtUtil.generateToken(username);
    }

    public void doLogout(String token) {
        if (StringUtils.hasText(token)) {
            jwtUtil.addToBlacklist(token);
        }
    }
}
