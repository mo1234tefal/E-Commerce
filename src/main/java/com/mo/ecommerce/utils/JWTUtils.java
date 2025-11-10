package com.mo.ecommerce.utils;

import com.mo.ecommerce.repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtils {
    @Value("${spring.application.secret_key}")
    private String secretKey;
    public Key signingKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
     }

    public String generateToken(UserDetails userDetails , Map<String , Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(signingKey())
                .compact();

    }
    public String generateToken(UserDetails userDetails){return generateToken(userDetails , new HashMap<>());}

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(signingKey()).build()
                .parseClaimsJws(token).getBody();
    }
    private<T> T extractClaim(String token , Function<Claims , T> claimsResolver){
        return claimsResolver.apply(extractAllClaims(token));
    }
    public boolean isExpired(String token){
        return extractClaim(token , Claims::getExpiration).before(new Date());
    }
    public String extractUsername(String token){
        return extractClaim(token , Claims::getSubject);
    }
    public boolean isTokenValid(String token , UserDetails userDetails){
        return userDetails.getUsername().equals(extractUsername(token)) && !isExpired(token);
    }


}
