package com.scm2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.scm2.security.JwtAUthenticationEntryPoint;
import com.scm2.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private JwtAuthenticationFilter jwtAuthenticationfilter;
    private JwtAUthenticationEntryPoint jwtAUthenticationEntryPoint;

    public SecurityConfig(UserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationfilter, JwtAUthenticationEntryPoint jwtAUthenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationfilter = jwtAuthenticationfilter;
        this.jwtAUthenticationEntryPoint = jwtAUthenticationEntryPoint;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationManagerConfiguration)
            throws Exception {
        return authenticationManagerConfiguration.getAuthenticationManager();

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(HttpMethod.POST, "/user").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/user/**").hasRole("USER")
                        .requestMatchers("/outh2/**").hasAnyRole("USER")
                        .anyRequest().permitAll())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAUthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationfilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf(csrf -> csrf.disable());
        http.oauth2Login(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();

    }

}
