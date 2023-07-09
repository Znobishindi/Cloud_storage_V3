package com.example.cloud_storage_v3.security;

import com.example.cloud_storage_v3.entity.User;
import com.example.cloud_storage_v3.exceptions.UserNotFoundException;
import com.example.cloud_storage_v3.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtProvider {
    private final UserRepository userRepository;
    private final SecretKey jwtAccessSecret;
    private User authUser;

    public User getAuthorizedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authUser = userRepository.findByLogin(authentication.getName()).orElseThrow(() ->
                new UserNotFoundException("Пользователя с таким логином нет в базе данных", 0));

        return authUser;
    }

    @Autowired
    public JwtProvider(
            @Value("${jwt.secret.access}")
            String jwtAccessSecret, UserRepository userRepository) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.userRepository = userRepository;
    }

    public String generateAccessToken(@NonNull User user) {
        LocalDateTime now = LocalDateTime.now();
        Instant accessExpirationInstant = now.plusMinutes(5)
                .atZone(ZoneId.systemDefault()).toInstant();
        Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setId(String.valueOf(user.getId()))
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("roles", user.getRole())
                .compact();
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
