package com.mygym.kamehouse.dto;

public class UserLoginDto {
    private String email;
    private String password;

    public UserLoginDto() {
        // Constructor por defecto
    }

    public UserLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters y setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}