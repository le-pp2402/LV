package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.exceptions.BadRequestException;
import com.phatpl.learnvocabulary.exceptions.ExistedException;
import com.phatpl.learnvocabulary.exceptions.ExpiredException;
import com.phatpl.learnvocabulary.utils.Logger;
import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

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
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
        Logger.log(Logger.GREEN + "TOKEN = " + token + Logger.RESET);
        return token;
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
                .signWith(SignatureAlgorithm.HS256, SecretKey)
                .compact();
        return newToken;
    }
}
