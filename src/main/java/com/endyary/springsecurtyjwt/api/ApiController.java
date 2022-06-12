package com.endyary.springsecurtyjwt.api;

import com.endyary.springsecurtyjwt.user.AuthService;
import com.endyary.springsecurtyjwt.user.CustomUserDetails;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
        
        Authentication authentication = authService.doAuth(username, password);
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        return String.format("Hello %s!", principal.getUser().getFullName());
    }

    @PostMapping("/public/loginjwt")
    public String loginJwt(@RequestBody JsonNode payload) {
        String username = payload.get("username").asText();
        String password = payload.get("password").asText();

        authService.doAuth(username, password);
        return authService.generateToken(username);
    }

    @GetMapping("/private/logout")
    public ResponseEntity<Long> logout(@RequestHeader(name = "Authorization") String token) {
        authService.doLogout(token.substring(7));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
