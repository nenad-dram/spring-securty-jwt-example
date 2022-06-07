package com.endyary.springsecurtyjwt.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User principal = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("User with username - %s, not found", username)));
        return new CustomUserDetails(principal);
    }

    public User insertNonExistent(User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        return optionalUser.isEmpty() ? userRepository.save(user) : user;
    }

}
