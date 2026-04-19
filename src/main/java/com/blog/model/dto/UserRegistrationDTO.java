package com.blog.model.dto;

import com.blog.model.UserType;
import jakarta.json.bind.annotation.JsonbProperty;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class UserRegistrationDTO {
    private String name;
    private String lastname;
    private LocalDate dtNasc;
    private String fone;
    private String email;
    private String cryptographyPassword;
    private UserType userType;

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDtNasc() {
        return dtNasc;
    }

    public void setDtNasc(LocalDate dtNasc) {
        this.dtNasc = dtNasc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getCryptographyPassword() {
        return cryptographyPassword;
    }

    public void setCryptographyPassword(String cryptographyPassword) {
        this.cryptographyPassword = cryptographyPassword;
    }
}
