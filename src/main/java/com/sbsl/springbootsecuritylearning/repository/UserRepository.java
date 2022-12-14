package com.sbsl.springbootsecuritylearning.repository;

import com.sbsl.springbootsecuritylearning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
