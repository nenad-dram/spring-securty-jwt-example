package com.endyary.springsecurtyjwt.configuration;

import com.endyary.springsecurtyjwt.user.User;
import com.endyary.springsecurtyjwt.user.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Inserts two users in the DB at the application startup
 */
@Component
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final List<String> usernames = List.of(
            "john.doe@mail.com",
            "jane.doe@mail.com");
    private final List<String> fullNames = List.of(
            "John Doe",
            "Jane Doe");
    private final List<User.Role> roles = List.of(
            new User.Role("USER"),
            new User.Role("ADMIN"));
    private final String password = "Test12345";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        
        for (int i = 0; i < usernames.size(); ++i) {
            User user = new User();
            user.setUsername(usernames.get(i));
            user.setPassword(passwordEncoder.encode(password));
            user.setFullName(fullNames.get(i));
            user.setRoles(Set.of(roles.get(i)));

            userService.insertNonExistent(user);
        }
    }

}
