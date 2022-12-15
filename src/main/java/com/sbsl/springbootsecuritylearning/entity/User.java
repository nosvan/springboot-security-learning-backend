package com.sbsl.springbootsecuritylearning.entity;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") }
    )
    private List<Role> roles = new ArrayList<>();

    public User() {
    }

    public User(@NonNull String name, @NonNull String email, @NonNull String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<String> getRolesString() {
        List<String> listOfRoles = new ArrayList<>();
        for(Role r: roles){
            listOfRoles.add(r.getName());
        }
        return listOfRoles;
    }
}
