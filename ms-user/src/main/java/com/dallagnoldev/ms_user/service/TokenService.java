package com.dallagnoldev.ms_user.service;

import com.dallagnoldev.ms_user.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenService {

    @Value(value = "${jwt.secret}")
    private String jwtSecret;

    @Value(value = "${jwt.expiration}")
    private long jwtExpiration;
    
    public String generateToken(UserModel userModel) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpiration);
        
        return Jwts.builder()
                .subject(userModel.getUsername())
                .claim("userId", userModel.getUserId().toString())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }
    
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
