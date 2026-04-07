package com.blog.model;

import java.util.Date;
import java.util.UUID;

public class User{
    private UUID id;
    private String name;
    private Date dt_nasc;
    private Integer fone;
    private String bio;
    private String email;
    private String criptography_password;
    private UserType user_type;

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            System.out.println("name field is null or blank");
            return;
        }
        this.name = name;
    }

    public Date getDt_nasc() {
        return dt_nasc;
    }

    public void setDt_nasc(Date dt_nasc) {
        this.dt_nasc = dt_nasc;
    }

    public Integer getFone() {
        return fone;
    }

    public void setFone(Integer fone) {
        this.fone = fone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUser_type() {
        return user_type;
    }

    public void setUser_type(UserType user_type) {
        this.user_type = user_type;
    }

    public String getCriptography_password() {
        return criptography_password;
    }

    public void setCriptography_password(String criptography_password) {
        this.criptography_password = criptography_password;
    }
}