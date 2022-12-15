package com.sbsl.springbootsecuritylearning.controller;
import com.sbsl.springbootsecuritylearning.dto.UserDto;
import com.sbsl.springbootsecuritylearning.dto.UserLoginDto;
import com.sbsl.springbootsecuritylearning.dto.UserRegisterDto;
import com.sbsl.springbootsecuritylearning.entity.User;
import com.sbsl.springbootsecuritylearning.jwt.JwtResponse;
import com.sbsl.springbootsecuritylearning.jwt.JwtUtil;
import com.sbsl.springbootsecuritylearning.service.UserServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // handler method to handle user registration form submit request
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginDto userLoginDto){
        log.info("/login " + userLoginDto.getEmail());
        User existingUser = userServiceImpl.findUserByEmail(userLoginDto.getEmail());

        if(existingUser == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        boolean matched = passwordEncoder.matches(userLoginDto.getPassword(), existingUser.getPassword());
        if(!matched) return new ResponseEntity(HttpStatus.NOT_FOUND);
        String token = jwtUtil.generateJwtToken(existingUser);
        return new ResponseEntity<>(createJwtResponse(existingUser, token),HttpStatus.OK);
    }

    private JwtResponse createJwtResponse(User user, String token){
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setId(user.getId());
        jwtResponse.setEmail(user.getEmail());
        jwtResponse.setToken(token);
        jwtResponse.setRoles(user.getRolesString());
        return jwtResponse;
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
        UserDto fetchedUserDto = new UserDto();
        fetchedUserDto.setFirstName(user.getFirstName());
        fetchedUserDto.setLastName(user.getLastName());
        fetchedUserDto.setEmail(user.getEmail());
        fetchedUserDto.setId(user.getId());
        return fetchedUserDto;
    }

    // handler method to handle list of users
    @GetMapping("/users")
    public ResponseEntity users(){
        log.info("/users");
        List<UserDto> users = userServiceImpl.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}