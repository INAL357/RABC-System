package com.app.auth.auth_app_backend.security;

import com.app.auth.auth_app_backend.entities.Role;
import com.app.auth.auth_app_backend.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
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
    public String generateAccessToken(User user) {

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
                        "type", "access"           // Explicit token type tagging to prevent context-switching exploits
                ))

        // Cryptographically sign the header and payload metadata using your secret key.
        // JJWT dynamically infers the appropriate strength (HS512) directly from your 64+ byte key.
                .signWith(key, SignatureAlgorithm.HS512)

                // Serialize the entire signed payload structure into its final compact string format (xxxx.yyyy.zzzz)
                .compact();
    }

    public String generateRefreshToken(User user, String jti){
        Instant now = Instant.now();
        return Jwts.builder()
                .id(jti)
                .subject(user.getId().toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshTtlSeconds)))
                .claim("type", "refresh")
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }

    //Method to parse the token
    public Jws<Claims> parse(String token){
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);

    }
    public boolean isAccessToken(String token) {
        Claims claims = parse(token).getPayload();
        return "access".equals(claims.get("type", String.class));
    }

    public boolean isRefreshToken(String token) {
        Claims claims = parse(token).getPayload();
        return "refresh".equals(claims.get("type", String.class));
    }

    public UUID getUserId(String token) {
        Claims c = parse(token).getPayload();
        return UUID.fromString(c.getSubject());
    }

    public String getJTI(String token) {
        return parse(token).getPayload().getId();
    }

    public List<String> getRoles(String token) {
        Claims claims = parse(token).getPayload();
        return (List<String>) claims.get("roles");
    }

    public String getEmail(String token) {
        Claims claims = parse(token).getPayload();
        return claims.get("email", String.class);
    }
}


