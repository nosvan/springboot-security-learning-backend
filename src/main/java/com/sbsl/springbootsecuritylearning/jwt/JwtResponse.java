package com.sbsl.springbootsecuritylearning.jwt;

import com.sbsl.springbootsecuritylearning.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private List<String> roles;
}
