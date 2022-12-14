package com.sbsl.springbootsecuritylearning.repository;

import com.sbsl.springbootsecuritylearning.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
