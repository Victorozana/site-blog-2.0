package com.blog.model.dto;

import com.blog.model.UserType;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class UserRegistrationDTO {
    private String name;
    private LocalDate dt_nasc;
    private String fone;
    private String email;
    private String cryptography_password;
    private UserType user_type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDt_nasc() {
        return dt_nasc;
    }

    public void setDt_nasc(LocalDate dt_nasc) {
        this.dt_nasc = dt_nasc;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getcryptography_password() {
        return cryptography_password;
    }

    public void setcryptography_password(String cryptography_password) {
        this.cryptography_password = cryptography_password;
    }

    public UserType getUser_type() {
        return user_type;
    }

    public void setUser_type(UserType user_type) {
        this.user_type = user_type;
    }
}
