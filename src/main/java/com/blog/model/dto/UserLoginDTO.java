package com.blog.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {
    private String email;
    private String cryptographyPassword;
}
