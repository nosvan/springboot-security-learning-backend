package com.sbsl.springbootsecuritylearning.dto;

public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){ return id;}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
