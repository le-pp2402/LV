package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {

    private final String secretKey = "1TjXchw5FloESb63Kc+DFhTARvpWL4jUGCwfGWxuG5SIf/1y/LgJxHnMqaF6A/ij";
    private final Long expirationTime = 3 * 24 * 60 * 60 * 1000L;

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // subject = username
    public String getUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date getExpirationTime(String token) {
        return extractClaims(token, Claims::getExpiration);
    }


    public Boolean isExpired(String token) {
        return getExpirationTime(token).before(new Date());
    }

    public Boolean isValid(String token, User user) {
        final var username = getUsername(token);
        return (username.equals(user.getUsername()) && !isExpired(token));
    }


    public Key getKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(UserResponse userResponse) {
        return Jwts.builder().setIssuer("learnvocabulary")
                .setSubject(userResponse.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, getKey())
                .claim("scope", getScope(userResponse))
                .compact();
    }

    private String getScope(UserResponse userResponse) {
        return (userResponse.getIsAdmin() == true ? "USER ADMIN" : "USER");
    }
}
