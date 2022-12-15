package com.sbsl.springbootsecuritylearning.controller;
import com.sbsl.springbootsecuritylearning.dto.UserDto;
import com.sbsl.springbootsecuritylearning.entity.User;
import com.sbsl.springbootsecuritylearning.service.UserServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
public class AuthController {

    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder) {
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    // handler method to handle user registration form submit request
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserDto userDto){
        log.info("/login " + userDto.getEmail());
        User existingUser = userServiceImpl.findUserByEmail(userDto.getEmail());

        if(existingUser == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        boolean matched = passwordEncoder.matches(userDto.getPassword(), existingUser.getPassword());
        if(!matched) return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity registration(@RequestBody UserDto userDto){
        User existingUser = userServiceImpl.findUserByEmail(userDto.getEmail());

        if(existingUser == null){
            userServiceImpl.saveUser(userDto);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    // handler method to handle list of users
    @GetMapping("/users")
    public ResponseEntity users(){
        log.info("/users");
        List<UserDto> users = userServiceImpl.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}