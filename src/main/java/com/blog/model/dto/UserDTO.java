package com.blog.model.dto;

import com.blog.model.UserType;

import java.util.Date;

public class UserDTO {
    private String name;
    private String bio;

    public UserDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
