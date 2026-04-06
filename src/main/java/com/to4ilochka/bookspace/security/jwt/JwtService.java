package com.to4ilochka.bookspace.security.jwt;

import com.to4ilochka.bookspace.dto.security.JwtAuthenticationDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtService {
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiration}")
    private long jwtExpirationMs;
    @Value("${app.jwt.refreshExpiration}")
    private long refreshExpirationMs;

    public JwtAuthenticationDTO generateAuthToken(String email) {
        return new JwtAuthenticationDTO(
                generateJwtToken(email),
                generateRefreshToken(email)
        );
    }

    public JwtAuthenticationDTO refreshBaseToken(String email, String refreshToken) {
        return new JwtAuthenticationDTO(
                generateJwtToken(email),
                refreshToken
        );
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Expired JwtException", expEx);
        } catch (UnsupportedJwtException expEx) {
            log.error("Unsupported JwtException", expEx);
        } catch (MalformedJwtException expEx) {
            log.error("Malformed JwtException", expEx);
        } catch (SecurityException expEx) {
            log.error("Security Exception", expEx);
        } catch (Exception expEx) {
            log.error("Invalid token", expEx);
        }
        return false;
    }

    private String generateJwtToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }

    private String generateRefreshToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpirationMs);
        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
