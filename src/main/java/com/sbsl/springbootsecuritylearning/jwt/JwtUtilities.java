package com.sbsl.springbootsecuritylearning.jwt;

import com.sbsl.springbootsecuritylearning.entity.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Log4j2
public class JwtUtilities {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateJwtToken(User user){
        int jwtExpirationMs = 60 * 60;
        return Jwts.builder().setSubject(user.getId().toString()).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getIdFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String parseForToken(HttpServletRequest request){
        String setCookie = request.getHeader("Set-Cookie");
        if(setCookie == null) return null;
        String[] cookieArray = setCookie.split("; ");
        Optional<String> tokenWithJwtName = Arrays.stream(cookieArray).filter(e -> e.contains("user-jwt-cookie")).findFirst();
        if(tokenWithJwtName.isEmpty()) return null;
        return tokenWithJwtName.toString().split("=")[1];
    }
}