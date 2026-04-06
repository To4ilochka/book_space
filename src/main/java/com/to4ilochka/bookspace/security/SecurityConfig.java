package com.to4ilochka.bookspace.security;

import com.to4ilochka.bookspace.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        TODO authentication
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**").permitAll()

                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/ui/books/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/books/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasAnyRole("ADMIN", "EMPLOYEE")

                        .requestMatchers(HttpMethod.GET, "/api/clients/me").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/clients/me").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/clients/me/balance").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/api/clients/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.PATCH, "/api/clients/*/lock").hasAnyRole("ADMIN", "EMPLOYEE")

                        .requestMatchers(HttpMethod.GET, "/api/employees/me").hasRole("EMPLOYEE")
                        .requestMatchers("/api/employees/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/orders").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/api/orders/me").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/api/orders").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/*/status").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/orders/*").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
