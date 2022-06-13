package com.endyary.springsecurtyjwt.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Defines Spring security's configuration and required beans
 */
@EnableWebSecurity
public class SecurityConfig {

    private final JWTFilter jwfFilter;

    public SecurityConfig(JWTFilter jwfFilter) {
        this.jwfFilter = jwfFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
