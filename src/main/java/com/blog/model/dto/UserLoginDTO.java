package com.blog.model.dto;

import com.blog.model.UserType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserLoginDTO {
    private String email;
    private String cryptographyPassword;
}
