package com.sbsl.springbootsecuritylearning.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Log4j2
@Component
public class CookieUtilities {
    private final JwtUtilities jwtUtilities;

    public CookieUtilities(JwtUtilities jwtUtilities) {
        this.jwtUtilities = jwtUtilities;
    }

    public String parseCookieFromHeader(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        Optional<Cookie> cookieFiltered = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieName)).findFirst();
        if (cookieFiltered.isEmpty()) return null;
        return cookieFiltered.get().getValue();
    }

    public ResponseCookie createResponseCookie(String cookieName, Authentication authentication) {
        return ResponseCookie.from(cookieName, jwtUtilities.generateJwtToken(authentication))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(60 * 60 * 24)
                .domain("localhost")
                .build();
    }

    public ResponseCookie removeCookieFromResponse(String cookieName) {
        return ResponseCookie.from(cookieName).value(null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .domain("localhost")
                .build();
    }
}
