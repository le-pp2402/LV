package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.exceptions.BadRequestException;
import com.phatpl.learnvocabulary.exceptions.ExpiredException;
import com.phatpl.learnvocabulary.utils.Logger;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import static org.springframework.web.util.TagUtils.getScope;

@Service
@Getter
public class JWTService {

    @Value("${SECRET_KEY}")
    private String SecretKey;
    @Value("${EXPIRATION_TIME}")
    private Long EXPIRATION_TIME;

    public String genToken(UserResponse userResponse) {
        long current = System.currentTimeMillis();
        String token = Jwts.builder().setIssuer("learnvocabulary")
                .claim("data", userResponse)
                .setSubject(userResponse.getUsername())
                .setIssuedAt(new Date(current))
                .setExpiration(new Date(current + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, getKey())
                .claim("scope", getScope(userResponse))
                .compact();
        Logger.log(Logger.GREEN + "TOKEN = " + token + Logger.RESET);
        return token;
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private String getScope(UserResponse userResponse) {
        if (userResponse.getIsAdmin() == true) {
            return "USER ADMIN";
        } else {
            return "USER";
        }
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Jws<Claims> verifyToken(String token) {
        try {
            return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredException("token");
        } catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }

    public String refreshToken(String oldToken) {
        long current = System.currentTimeMillis();
        var data = (Map<String, Object>) verifyToken(oldToken).getBody().get("data");
        String newToken = Jwts.builder().setIssuer("learnvocabulary")
                .claim("data", data)
                .setSubject((String) data.get("username"))
                .setIssuedAt(new Date(current))
                .setExpiration(new Date(current + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, getKey())
                .compact();
        return newToken;
    }

    Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
