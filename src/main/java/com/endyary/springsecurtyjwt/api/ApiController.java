package com.endyary.springsecurtyjwt.api;

import com.endyary.springsecurtyjwt.user.AuthService;
import com.endyary.springsecurtyjwt.user.CustomUserDetails;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private final AuthService authService;

    public ApiController(AuthService authService) {
        this.authService = authService;
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

        CustomUserDetails principal;
        Authentication authentication = authService.doAuth(username, password);
        principal = (CustomUserDetails) authentication.getPrincipal();

        return String.format("Hello %s!", principal.getUser().getFullName());
    }

    @PostMapping("/public/loginjwt")
    public String loginJwt(@RequestBody JsonNode payload) {
        String username = payload.get("username").asText();
        String password = payload.get("password").asText();

        authService.doAuth(username, password);
        return authService.generateToken(username);
    }
}
