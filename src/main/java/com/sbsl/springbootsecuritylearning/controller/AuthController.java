package com.sbsl.springbootsecuritylearning.controller;
import com.sbsl.springbootsecuritylearning.dto.UserDto;
import com.sbsl.springbootsecuritylearning.dto.UserLoginDto;
import com.sbsl.springbootsecuritylearning.dto.UserRegisterDto;
import com.sbsl.springbootsecuritylearning.entity.User;
import com.sbsl.springbootsecuritylearning.jwt.JwtUtilities;
import com.sbsl.springbootsecuritylearning.repository.UserRepository;
import com.sbsl.springbootsecuritylearning.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Log4j2
@Controller
public class AuthController {
    private final UserServiceImpl userServiceImpl;
    private final JwtUtilities jwtUtilities;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserServiceImpl userServiceImpl, JwtUtilities jwtUtilities, AuthenticationManager authenticationManager) {
        this.userServiceImpl = userServiceImpl;
        this.jwtUtilities = jwtUtilities;
        this.authenticationManager = authenticationManager;
    }

    // handler method to handle user registration form submit request
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginDto userLoginDto, HttpServletResponse response){
        log.info("/login " + userLoginDto.getEmail());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));
        ResponseCookie springCookie = ResponseCookie.from("user-jwt-cookie", jwtUtilities.generateJwtToken(authentication))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(60*60*60)
                .domain("localhost")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, springCookie.toString()).body("haha");
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registration(@RequestBody UserRegisterDto userRegisterDto){
        User existingUser = userServiceImpl.findUserByEmail(userRegisterDto.getEmail());
        if(existingUser == null){
            userServiceImpl.saveUser(userRegisterDto);
            User fetchedUser = userServiceImpl.findUserByEmail(userRegisterDto.getEmail());
            return new ResponseEntity(UserToUserDto(fetchedUser),HttpStatus.OK);
        }
        return new ResponseEntity(UserToUserDto(existingUser),HttpStatus.CONFLICT);
    }

    private UserDto UserToUserDto(User user){
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    // handler method to handle list of users
    @GetMapping("/users")
    public ResponseEntity users(){
        log.info("/users");
        List<UserDto> users = userServiceImpl.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}