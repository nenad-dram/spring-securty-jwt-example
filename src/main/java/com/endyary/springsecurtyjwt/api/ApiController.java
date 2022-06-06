package com.endyary.springsecurtyjwt.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

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
}
