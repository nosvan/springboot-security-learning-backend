package com.sbsl.springbootsecuritylearning.jwt;

import com.sbsl.springbootsecuritylearning.service.UserDetailsImpl;
import com.sbsl.springbootsecuritylearning.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtUtilities jwtUtilities;
    private final CookieUtilities cookieUtilities;

    public JwtRequestFilter(UserDetailsServiceImpl userDetailsServiceImpl, JwtUtilities jwtUtilities, CookieUtilities cookieUtilities) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtUtilities = jwtUtilities;
        this.cookieUtilities = cookieUtilities;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = cookieUtilities.parseCookieFromHeader(request, "user-jwt-cookie");
        String email = null;
        log.info(requestTokenHeader);
        if (requestTokenHeader != null) {
            try {
                email = jwtUtilities.getUserFromToken(requestTokenHeader);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtilities.validateJwtToken(requestTokenHeader)) {
                UserDetailsImpl userDetailsImpl = userDetailsServiceImpl.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetailsImpl, null, userDetailsImpl.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}