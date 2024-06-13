package com.phatpl.learnvocabulary.services;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import com.phatpl.learnvocabulary.utils.Logger;
import io.jsonwebtoken.*;

import java.util.Date;

public class JWTService {

    private static String SecretKey = "CoickW+pfQ+aKlH016EotzHYff2qUtGKN+UtQ3ETWjc=";
    private static final Long EXPIRATION_TIME = 3 * 24 * 60 * 60 * 1000L;

    public static String genToken(UserResponse userResponse) {
        Long current = System.currentTimeMillis();
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


    public static Jws<Claims> verifyToken(String token) throws Exception {
        return Jwts.parser().setSigningKey(SecretKey).parseClaimsJws(token);
    }
}
