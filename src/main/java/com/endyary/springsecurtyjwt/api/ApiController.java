package com.endyary.springsecurtyjwt.api;

import com.endyary.springsecurtyjwt.configuration.JWTUtil;
import com.endyary.springsecurtyjwt.user.CustomUserDetails;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private final AuthenticationManager authManager;

    private final JWTUtil jwtUtil;

    public ApiController(AuthenticationManager authManager, JWTUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/public/hello")
    public String getPublicMessage() {
        return "Hello Guest!";
    }

    @GetMapping("/user/hello")
    public String getUserMessage() {
        return "Hello User!";
    }

    @GetMapping("/admin/hello")
    public String getAdminMessage() {
        return "Hello Admin!";
    }

    @PostMapping("/public/login")
    public String login(@RequestBody JsonNode payload) {
        String username = payload.get("username").asText();
        String password = payload.get("password").asText();

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(username, password);

        CustomUserDetails principal;
        try {
            Authentication authentication = authManager.authenticate(authInputToken);
            principal = (CustomUserDetails) authentication.getPrincipal();
        } catch (AuthenticationException authExc) {
            return "Invalid username or password!";
        }

        return String.format("Hello %s!", principal.getUser().getFullName());
    }

    @PostMapping("/public/loginjwt")
    public String loginJwt(@RequestBody JsonNode payload) {
        String username = payload.get("username").asText();
        String password = payload.get("password").asText();

        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(username, password);

            authManager.authenticate(authInputToken);

            return jwtUtil.generateToken(username);
        } catch (AuthenticationException authExc) {
            return "Invalid username or password!";
        }
    }
}
