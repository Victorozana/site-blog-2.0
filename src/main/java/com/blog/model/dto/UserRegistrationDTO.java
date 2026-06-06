package com.blog.model.dto;

import com.blog.model.category.UserType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UserRegistrationDTO {
    private String name;
    private String lastname;
    private LocalDate dtNasc;
    private String fone;
    private String email;
    private String password;
    private UserType userType;
}
