package com.blog.model.dto;

import com.blog.model.UserType;

import java.time.LocalDate;

public class UserLoginDTO {
    private String email;
    private String cryptographyPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCryptographyPassword() {
        return cryptographyPassword;
    }

    public void setCryptographyPassword(String cryptographyPassword) {
        this.cryptographyPassword = cryptographyPassword;
    }
}
