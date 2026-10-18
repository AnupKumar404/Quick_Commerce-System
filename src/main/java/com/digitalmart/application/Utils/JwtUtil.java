package com.digitalmart.application.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-expiry}")
    private long ACCESS_TOKEN_EXPIRY;

    @Value("${jwt.refresh-expiry}")
    private long REFRESH_TOKEN_EXPIRY;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Completed Refresh Token Generator
    public String generateRefreshToken(UserPrincipal user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("user_id", user.getUserId())
                .claim("token_type", "refresh_token") // Crucial for Option C isolation
                .header().empty().add("typ", "jwt")
                .and()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
                .signWith(getSigningKey())
                .compact();
    }

    // Updated Access Token Generator
    public String generateToken(UserPrincipal user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("user_id", user.getUserId())
                .claim("user_role", user.getAuthorities().toString())
                .claim("token_type", "access_token") // Added to prevent Refresh Tokens from being used as Access Tokens
                .header().empty().add("typ", "jwt")
                .and()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                .signWith(getSigningKey())
                .compact();
    }

    // Extract all claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Validate Access Token (Use this in your JwtAuthenticationFilter)
    public boolean validateAccessToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String tokenType = claims.get("token_type", String.class);
            return "access_token".equals(tokenType);
        } catch (Exception e) {
            return false;
        }
    }

    // Validate Refresh Token (Use this in your /refresh endpoint controller)
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String tokenType = claims.get("token_type", String.class);
            return "refresh_token".equals(tokenType);
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }

    public boolean checkExpiryOfToken(String token){
        return !extractExpiration(token).before(new Date());
    }

    // Fallback basic validation
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}