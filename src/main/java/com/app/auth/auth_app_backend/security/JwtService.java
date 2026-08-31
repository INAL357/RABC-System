package com.app.auth.auth_app_backend.security;

import com.app.auth.auth_app_backend.entities.Role;
import com.app.auth.auth_app_backend.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Signature;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class JwtService {
    private final SecretKey key;

    private final String issuer;
    private final long accessTtlSeconds;
    private final long refreshTtlSeconds;


    public JwtService(
            @Value("${security.jwt.secret}") String secret,

            @Value("${security.jwt.issuer}") String issuer,
            @Value("${security.jwt.access-ttl-seconds}") long accessTtlSeconds,
            @Value("${security.jwt.refresh-ttl-seconds}") long refreshTtlSeconds
    ) {

        if(secret == null || secret.length()<64){
            throw new IllegalArgumentException("Invalid secret");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.refreshTtlSeconds=refreshTtlSeconds;
        this.accessTtlSeconds = accessTtlSeconds;
        this.issuer = issuer;

    }

    //generate token method
    public String generateToken(User user) {

        // returns the exact same global timestamp
        Instant now = Instant.now();

        //check if user has roles,if yes then map and put it in the list or return empty list
        List<String> roles = user.getRoles() == null? List.of():
                user.getRoles().stream().map(Role::getName).toList();

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                // 'exp' (Expiration Time) Claim: Exact timestamp after which the token becomes invalid
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))

                // Public & Private Custom Claims payload body
                .claims(Map.of(
                        "email", user.getEmail(), // Useful client-facing identifier
                        "roles", roles,
                        "typ", "access"           // Explicit token type tagging to prevent context-switching exploits
                ))

        // Cryptographically sign the header and payload metadata using your secret key.
        // JJWT dynamically infers the appropriate strength (HS512) directly from your 64+ byte key.
                .signWith(key, SignatureAlgorithm.HS512)

                // Serialize the entire signed payload structure into its final compact string format (xxxx.yyyy.zzzz)
                .compact();
    }

    public String generateRefershToken(){
        return null;
    }
}


