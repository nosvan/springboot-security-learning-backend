package com.sbsl.springbootsecuritylearning.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private Long id;
    private String email;
    private List<String> roles;
    private String type;
    private String token;

}
