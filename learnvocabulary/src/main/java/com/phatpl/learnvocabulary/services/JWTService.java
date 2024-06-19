package com.phatpl.learnvocabulary.services;

import com.phatpl.learnvocabulary.dtos.response.UserResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JWTService {

    private final String secretKey = "BB4F994CF6B35773CA792F3911E4F";
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

    public Boolean isValid(String token, UserDetails userDetails) {
        final var username = getUsername(token);
        return (username.equals(userDetails.getUsername()) && !isExpired(token));
    }


    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(HashMap<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setIssuer("learnvocabulary")
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, getKey())
                .compact();
    }

    public String genDefaultUserToken(UserResponse userResponse, UserDetails userDetails) {
        HashMap<String, Object> extraClaims = new HashMap<String, Object>();
        extraClaims.put("id", userResponse.getId());
        extraClaims.put("email", userResponse.getEmail());
        extraClaims.put("is_admin", userResponse.getIsAdmin());
        extraClaims.put("elo", userResponse.getElo());
        return createToken(extraClaims, userDetails);
    }

}
