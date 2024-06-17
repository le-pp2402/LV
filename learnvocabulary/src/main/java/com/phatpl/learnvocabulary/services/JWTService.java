package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.models.User;
import com.phatpl.learnvocabulary.utils.Logger;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;

public class JWTService {

    private static final String SecretKey = "CoickW+pfQ+aKlH016EotzHYff2qUtGKN+UtQ3ETWjc=";
    private static final Long EXPIRATION_TIME = 1000L;
            // 3 * 24 * 60 * 60 * 1000L;

    public static String genToken(UserResponse userResponse) {
        long current = System.currentTimeMillis();
        String token = Jwts.builder().setIssuer("learnvocabulary")
                .claim("data", userResponse)
                .setSubject(userResponse.getUsername())
                .setIssuedAt(new Date(current))
                .setExpiration(new Date(current + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SecretKey)
                .compact();
        Logger.log(Logger.GREEN + "TOKEN = " + token + Logger.RESET);
        return token;
    }


    public static Jws<Claims> verifyToken(String token) {
        try {
            return Jwts.parser().setSigningKey(SecretKey).parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Login session expired");
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String refreshToken(String oldToken) {
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
