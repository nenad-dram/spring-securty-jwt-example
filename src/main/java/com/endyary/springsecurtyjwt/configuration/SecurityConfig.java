package com.endyary.springsecurtyjwt.configuration;

import com.endyary.springsecurtyjwt.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Defines Spring's security configuration and required beans
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final JWTFilter jwfFilter;

    public SecurityConfig(UserService userService, JWTFilter jwfFilter) {
        this.userService = userService;
        this.jwfFilter = jwfFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Set UserDetailsService that will be used for authentication
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()// Enable Basic Auth
                .and()
                .csrf().disable() // Disable CSRF
                .cors() // Enable CORS
                .and()
                .authorizeRequests()
                .antMatchers("/public/**").permitAll() // Allow without auth
                .antMatchers("/private/**").authenticated() // Require authentication
                .antMatchers("/user/**").hasAuthority("USER") // Require "USER" role
                .antMatchers("/admin/**").hasAuthority("ADMIN") // Require "ADMIN" role
                .and()
                .exceptionHandling() // Exception handlers for Spring Security's exceptions
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Disable sessions

        // Set JWTFilter before UsernamePasswordAuthenticationFilter, i.e. it must be executed before
        http.addFilterBefore(jwfFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
}
