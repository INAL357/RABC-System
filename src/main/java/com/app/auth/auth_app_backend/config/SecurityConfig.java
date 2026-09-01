package com.app.auth.auth_app_backend.config;

import com.app.auth.auth_app_backend.security.JWTAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JWTAuthenticationFilter JWTAuthenticationFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/c1/auth/register").permitAll()
                        .requestMatchers("/api/c1/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
        .exceptionHandling(ex->ex.authenticationEntryPoint((request, response, auth ) -> {
            auth.printStackTrace();;
            response.setStatus(401);
            response.setContentType("application/Json");
            String message = "unauthorized access: "+auth.getMessage();
            Map<String,String> errorMap = Map.of("message",message, "status",String.valueOf(401),"statuscode", Integer.toString(401));
            var ObjectMapper = new ObjectMapper();
            response.getWriter().write(ObjectMapper.writeValueAsString(errorMap));
        }))
                .addFilterBefore(JWTAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}