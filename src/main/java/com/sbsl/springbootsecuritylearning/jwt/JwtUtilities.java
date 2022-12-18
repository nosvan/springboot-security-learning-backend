package com.sbsl.springbootsecuritylearning.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Log4j2
public class JwtUtilities {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateJwtToken(Authentication authentication) {
        Date expirationDate = new Date(new Date().getTime() + 1000 * 60 * 60 * 24);
        log.info(expirationDate.toString());
        return Jwts.builder().setSubject(authentication.getName()).setIssuedAt(new Date())
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserFromToken(String token) {
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
}