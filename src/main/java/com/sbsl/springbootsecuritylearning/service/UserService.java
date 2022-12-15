package com.sbsl.springbootsecuritylearning.service;

import com.sbsl.springbootsecuritylearning.dto.UserDto;
import com.sbsl.springbootsecuritylearning.dto.UserRegisterDto;
import com.sbsl.springbootsecuritylearning.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(UserRegisterDto userRegisterDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
