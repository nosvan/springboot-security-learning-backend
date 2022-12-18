package com.sbsl.springbootsecuritylearning.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Log4j2
@Component
public class CookieUtilities {
    public String parseCookieFromHeader(HttpServletRequest request, String cookieName) {
        Optional<Cookie> cookieFiltered = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(cookieName)).findFirst();
        if (cookieFiltered.isEmpty()) return null;
        return cookieFiltered.get().getValue();
    }
}
