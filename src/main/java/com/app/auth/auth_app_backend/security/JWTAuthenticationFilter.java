package com.app.auth.auth_app_backend.security;

import com.app.auth.auth_app_backend.helper.UserHelper;
import com.app.auth.auth_app_backend.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    public JWTAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        logger.info("Authorization header: {}", header);

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            try {

                // Check if token is an access token
                if (!jwtService.isAccessToken(token)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                Jws<Claims> parse = jwtService.parse(token);

                Claims payload = parse.getPayload();

                String userId = payload.getSubject();

                UUID userUUID = UserHelper.parseUUID(userId);

                userRepository.findById(userUUID)
                        .ifPresent(user -> {

                            // User must be enabled
                            if (!user.isEnabled()) {
                                return;
                            }

                            List<GrantedAuthority> authorities =
                                    user.getRoles() == null
                                            ? List.of()
                                            : user.getRoles()
                                            .stream()
                                            .map(role ->
                                                    new SimpleGrantedAuthority(
                                                            role.getName()
                                                    )
                                            )
                                            .collect(Collectors.toList());

                            if (SecurityContextHolder
                                    .getContext()
                                    .getAuthentication() == null) {

                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(
                                                user.getEmail(),
                                                null,
                                                authorities
                                        );

                                authentication.setDetails(
                                        new WebAuthenticationDetailsSource()
                                                .buildDetails(request)
                                );

                                SecurityContextHolder
                                        .getContext()
                                        .setAuthentication(authentication);
                            }
                        });

            } catch (ExpiredJwtException e) {
                e.printStackTrace();

            } catch (MalformedJwtException e) {
                e.printStackTrace();

            } catch (JwtException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }
}