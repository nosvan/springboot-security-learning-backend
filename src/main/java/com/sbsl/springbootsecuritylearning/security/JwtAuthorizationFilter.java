package com.sbsl.springbootsecuritylearning.security;

import com.sbsl.springbootsecuritylearning.entity.User;
import com.sbsl.springbootsecuritylearning.jwt.JwtUtilities;
import com.sbsl.springbootsecuritylearning.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtilities jwtUtilities;
    private final UserServiceImpl userServiceImpl;

    public JwtAuthorizationFilter(JwtUtilities jwtUtilities, UserServiceImpl userServiceImpl) {
        this.jwtUtilities = jwtUtilities;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtilities.parseForToken(request);
        if(token == null) filterChain.doFilter(request, response);

        boolean validToken = jwtUtilities.validateJwtToken(token);
        if(!validToken) filterChain.doFilter(request, response);

        Long id = Long.valueOf(jwtUtilities.getIdFromJwtToken(token));
        Optional<User> user = userServiceImpl.findById(id);
        if(user.isEmpty()) filterChain.doFilter(request, response);
        log.info(token);
//
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        Authentication anAuthentication = new Authentication(user);
//        context.setAuthentication(anAuthentication);
//        SecurityContextHolder.setContext(context);
//        Authentication authentication
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        if (tokenHelper.validateToken(authToken, userDetails)) {
//            // create authentication
//            TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
//            authentication.setToken(authToken);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }



        filterChain.doFilter(request, response);
    }
}
